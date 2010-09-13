package org.annolab.tt4j;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public
class DefaultModelResolverTest
{
	@Test
	public
	void testGetModel()
	throws Exception
	{
		verifyModel("src/test/resources/dummyModel.par", null, true);
		verifyModel("src/test/resources/dummyModel.par", "UTF-8", true);
		verifyModel("C:\\dummyModel.par", null, false);
		verifyModel("C:/dummyModel.par", null, false);
		verifyModel("C:\\dummyModel.par", "UTF-8", false);
		verifyModel("C:/dummyModel.par", "UTF-8", false);
	}

	@Test(expected=IOException.class)
	public
	void testGetModelFail()
	throws Exception
	{
		verifyModel("file:src/test/resources/dummyModel.par", null, true);
	}


	private
	void verifyModel(
			final String aModelPath,
			final String aEncoding,
			final boolean aCheckExistence)
	throws IOException
	{
		String modelName = aModelPath;
		if (aEncoding != null) {
			modelName = modelName + ":" + aEncoding;
		}
		DefaultModelResolver res = new DefaultModelResolver();
		res._checkExistence = aCheckExistence;
		Model model = res.getModel(modelName);

		assertEquals(aModelPath, model.getFile().getPath());
		assertEquals(modelName, model.getName());
		assertEquals(aEncoding != null ? aEncoding : "UTF-8", model.getEncoding());
	}
}
