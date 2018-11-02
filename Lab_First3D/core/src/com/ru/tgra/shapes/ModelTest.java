package com.ru.tgra.shapes;

import java.nio.FloatBuffer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.collision.BoundingBox;

public class ModelTest {
	AssetManager assets = new AssetManager();
	Model bobModel;

	private static FloatBuffer vertexBuffer;
	private static FloatBuffer normalBuffer;
	private static FloatBuffer UVBuffer;
	public ModelTest()
	{
		assets.load("models/bob/bob.g3dj", Model.class);
		assets.finishLoading();
		bobModel = assets.get("models/bob/bob.g3dj");
		BoundingBox bb = new BoundingBox();
		bobModel.meshes.first().calculateBoundingBox(bb, 0, 40);
		System.out.println("testing model loading: " + bobModel.meshes.first());
		System.out.println("more model testing: " + bobModel.nodes.get(4).id);
		ModelInstance md = new ModelInstance(bobModel);
		//md.getMaterial("test").get(0).
		//Renderable rend = new Renderable();
		//rend.
		//md.getRenderable(out, node)
	}
}
