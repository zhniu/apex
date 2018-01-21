/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.net3.viz

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import org.apache.commons.lang3.Validate
import org.stringtemplate.v4.ST

class VizRunYed {

		/** The command to start a WS from java. */
	public static final String CMD_JAVAWS = "javaws";

	/** The option to open a file. */
	public static final String CMD_OPTION = "-open";

	/** The yED JNLP file to load. */
	public static final String JNLP = "http://www.yworks.com/products/yed/demo/yed.jnlp";

	/** A flag for simulation mode. */
	protected boolean simulate;

	/**
	 * Sets simulation mode.
	 * @param simulate new simulation mode
	 * @return self to allow chaining
	 */
	def VizRunYed setSimulate(boolean simulate){
		this.simulate = simulate;
		return this;
	}

	/**
	 * Starts yED with a graph encoded in a template.
	 * @param graph the graph, must not be null and fully filled in
	 */
	def void startYed(ST graph){
		Validate.notNull(graph);
		val content = graph.render();
		if(this.simulate==false){
			try {
				val tmpFile = File.createTempFile("apex-gp-", "yed.graphml");
				//tmpFile.deleteOnExit();//TODO might be a problem
				this.writeFile(tmpFile, content);
				val Process p = new ProcessBuilder().command(CMD_JAVAWS, CMD_OPTION, tmpFile.toPath().toString(), JNLP).start();
				p.waitFor();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			//TODO something better than syserr?
			System.err.println(CMD_JAVAWS + " " + CMD_OPTION + " " + "TMP_FILE.graphml" + " " + JNLP);
		}
	}

	/**
	 * Writes a string to a file.
	 * @param file the target file, must not be null
	 * @param content the content to write, must not be blank
	 */
	def void writeFile(File file, String content){
		Validate.notNull(file, "writeFile: file cannot be blank");
		Validate.notBlank(content, "writeFile: content cannot be blank");

		var FileWriter fw = null;
		var BufferedWriter bw = null;
		try{
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(content);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(bw!==null){
					bw.close();
				}
				if(fw!==null){
					fw.close();
				}
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}