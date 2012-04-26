package org.annolab.tt4j;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Assume;
import org.junit.Test;

public class TreeTaggerWrapperTest
{
	{
		TreeTaggerWrapper.TRACE = true;
	}
	
	@Test
	public void testEnglish() throws Exception
	{
		List<String> actual = run(new TreeTaggerWrapper<String>(), 
		        "english-par-linux-3.2.bin:iso8859-1",
		        "This", "is", "a", "test", ".");
		
		List<String> expected = asList(
		        "This DT this", 
		        "is VBZ be", 
		        "a DT a", 
		        "test NN test", 
		        ". SENT .");
		
		assertEquals(expected, actual);
	}

	@Test
	public void testGermanText() throws Exception
	{
		String text = Util.readFile(new File("src/test/resources/text/test-de.txt"), "UTF-8");
		String actual = Util.join(run(new TreeTaggerWrapper<String>(), 
		        "german-par-linux-3.2-utf8.bin:utf-8",
		        Util.tokenize(text, Locale.GERMAN)), "\n");
		
		String expected = Util.readFile(new File("src/test/resources/text/test-de-expected.txt"), "UTF-8");

		assertEquals(expected, actual);
	}

	@Test
	public void testEnglishWithProbabilities() throws Exception
	{
		TreeTaggerWrapper<String> tt = new TreeTaggerWrapper<String>();
		tt.setProbabilityThreshold(0.1);
		List<String> actual =  run(tt, "english-par-linux-3.2.bin:iso8859-1", 
		        "This", "is", "a", "test", ".");
		
		List<String> expected = asList(
                "This DT this 1.0", 
                "is VBZ be 1.0", 
                "a DT a 1.0", 
                "test NN test 0.999661", 
                ". SENT . 1.0");
        
        assertEquals(expected, actual);
	}

   @Test
    public void testEnglishWithProbabilities2() throws Exception
    {
        TreeTaggerWrapper<String> tt = new TreeTaggerWrapper<String>();
        tt.setProbabilityThreshold(0.1);
        List<String> actual =  run(tt, "english-par-linux-3.2.bin:iso8859-1", 
                "He", "could", "lead", "if", "he", "would", "get", "the", "lead", "out", ".");
        
        List<String> expected = asList(
                "He PP he 1.0",
                "could MD could 1.0",
                "lead VV lead 0.999748",
                "if IN if 1.0",
                "he PP he 1.0",
                "would MD would 1.0",
                "get VV get 1.0",
                "the DT the 0.999993",
                "lead NN lead 0.753085",
                "lead VV lead 0.103856",
                "out RP out 0.726204",
                "out IN out 0.226546",
                ". SENT . 1.0");
        
        assertEquals(expected, actual);
    }

   @Test
   public void testEnglishWithProbabilities4() throws Exception
   {
       TreeTaggerWrapper<String> tt = new TreeTaggerWrapper<String>();
       tt.setProbabilityThreshold(0.1);
       List<String> actual =  run(tt, "english-par-linux-3.2.bin:iso8859-1", 
               "lead");
       
       List<String> expected = asList(
               "lead NN lead 0.647454",
               "lead VV lead 0.196787",
               "lead JJ lead 0.142647");
       
       assertEquals(expected, actual);
   }
	

	private List<String> run(final TreeTaggerWrapper<String> aWrapper, final String aModel, 
	        final String... aTokens)
		throws IOException, TreeTaggerException
	{
		Assume.assumeTrue(System.getenv("TREETAGGER_HOME") != null);
		
		try {
			final List<String> output = new ArrayList<String>();
			aWrapper.setModel(aModel);
			aWrapper.setHandler(new ProbabilityHandler<String>()
			{
			    private String token;
			    
				public void token(String aToken, String aPos, String aLemma)
				{
				    token = aToken;
				    if (aWrapper.getProbabilityThreshold() == null) {
	                    output.add(aToken + " " + aPos + " " + aLemma);
				    }
				}
				
				public void probability(String pos, String lemma, double probability)
				{
                    output.add(token + " " + pos + " " + lemma + " " + probability);
				}
			});
			aWrapper.process(aTokens);
			
			for (String o : output) {
			    System.out.println(o);
			}
			
			return output;
		}
		finally {
			aWrapper.destroy();
		}
	}
}
