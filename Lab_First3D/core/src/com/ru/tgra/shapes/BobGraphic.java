package com.ru.tgra.shapes;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.BufferUtils;

public class BobGraphic {
	static AssetManager assets = new AssetManager();
	static Model bobModel;
	static Texture tex;

	static HashMap<Integer, CustomMesh> meshMap;
	static HashMap<String, ShortBuffer> meshPartIndices;
	
	public static void create()
	{
		assets.load("models/bob/bob.g3dj", Model.class);
		assets.finishLoading();
		bobModel = assets.get("models/bob/bob.g3dj");
		BoundingBox bb = new BoundingBox();
		bobModel.meshes.first().calculateBoundingBox(bb, 0, 40);
		
		meshMap = new HashMap<Integer, CustomMesh>();
		meshPartIndices = new HashMap<String, ShortBuffer>();
		
		handleModel();
	}
	
	private static void handleModel() {
		for(Node node : bobModel.nodes) {
			//mesh.parts.first().material.id;
			if(node.parts.size > 0) {
				for(NodePart nodePart : node.parts) {
					Mesh newMesh = nodePart.meshPart.mesh;
					
					ShortBuffer indexBuf = BufferUtils.newShortBuffer(nodePart.meshPart.numVertices);
					short[] indexSlice = new short[nodePart.meshPart.numVertices];
					
					newMesh.getIndicesBuffer().get(indexSlice, 0, nodePart.meshPart.numVertices);
					indexBuf.put(indexSlice);
					indexBuf.rewind();
					meshPartIndices.put(nodePart.meshPart.id, indexBuf);
					
					//Create a new custom mesh using floatbuffers
					if(! meshMap.containsKey(nodePart.meshPart.mesh.hashCode())) {
						
						int vertexFill = 0;
						int positionStart = -1, positionEnd = -1;
						int normalStart = -1, normalEnd = -1;
						int uvStart = -1, uvEnd = -1;
						for(VertexAttribute vAttribute :nodePart.meshPart.mesh.getVertexAttributes()) {
								
							vertexFill += vAttribute.numComponents;
							if(vAttribute == newMesh.getVertexAttribute(Usage.Normal)) {
								normalStart = vertexFill - vAttribute.numComponents;
								normalEnd = vertexFill;
							}
							else if(vAttribute == newMesh.getVertexAttribute(Usage.Position)) {
								positionStart = vertexFill - vAttribute.numComponents;
								positionEnd = vertexFill;
							}
							else if(vAttribute == newMesh.getVertexAttribute(Usage.TextureCoordinates)) {
								uvStart = vertexFill - vAttribute.numComponents;
								uvEnd = vertexFill;
							}
						}
						FloatBuffer vertices = newMesh.getVerticesBuffer();
						int vertexPieceCount = vertices.capacity();
						int vertexLines = vertexPieceCount / vertexFill;
						CustomMesh newCustomMesh = new CustomMesh();
						
						newCustomMesh.normalBuffer = BufferUtils.newFloatBuffer(vertexLines*3);
						newCustomMesh.positionBuffer = BufferUtils.newFloatBuffer(vertexLines*3);
						newCustomMesh.uvBuffer = BufferUtils.newFloatBuffer(vertexLines*2);
						for(int i = 0; i < vertexPieceCount; i++) {	
							if(i % vertexFill >= positionStart && i % vertexFill < positionEnd) {
								newCustomMesh.positionBuffer.put(vertices.get(i));
							}
							else if(i % vertexFill >= normalStart && i % vertexFill < normalEnd) {
								newCustomMesh.normalBuffer.put(vertices.get(i));
							}
							else if(i % vertexFill >= uvStart && i % vertexFill  < uvEnd) {
								newCustomMesh.uvBuffer.put(vertices.get(i));
							}
						}
						newCustomMesh.positionBuffer.rewind();
						newCustomMesh.normalBuffer.rewind();
						newCustomMesh.uvBuffer.rewind();
						meshMap.put(newMesh.hashCode(), newCustomMesh);
					}
				}
			}
		}
	}
	public static void draw() {		
		for(Node node : bobModel.nodes) {
			if(node.parts.size > 0) {
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addTranslation(node.translation.x, node.translation.y, node.translation.z);
				ModelMatrix.main.addScale(node.scale.x, node.scale.y, node.scale.z);
				//SphereGraphic.drawOutlineSphere();
				for(NodePart nodePart : node.parts) {
					MeshPart meshPart = nodePart.meshPart;
					CustomMesh mesh = meshMap.get(meshPart.mesh.hashCode());
					
					TextureAttribute texMat = (TextureAttribute)nodePart.material.get(TextureAttribute.Diffuse);
					tex = texMat.textureDescription.texture;
					
					Shader.mainShader.setDiffuseTexture(tex);
					Shader.mainShader.setModelMatrix(ModelMatrix.main.matrix);
					Gdx.gl.glVertexAttribPointer(Shader.mainShader.getVertexPointer(), 3, GL20.GL_FLOAT, false, 0, mesh.positionBuffer);
					Gdx.gl.glVertexAttribPointer(Shader.mainShader.getNormalPointer(), 3, GL20.GL_FLOAT, false, 0, mesh.normalBuffer);
					Gdx.gl.glVertexAttribPointer(Shader.mainShader.getUVPointer(), 2, GL20.GL_FLOAT, false, 0, mesh.uvBuffer);
					
					Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, meshPartIndices.get(meshPart.id).capacity(), GL20.GL_UNSIGNED_SHORT, meshPartIndices.get(meshPart.id));
				}
				ModelMatrix.main.popMatrix();
			}
		}
	}
}
