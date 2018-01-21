/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.net3.basics

import java.util.regex.Matcher
import java.util.regex.Pattern
import org.apache.commons.lang3.Validate
import org.eclipse.xtend.lib.annotations.Data

@Data class Net3Identity implements Net3DoesValidate {

	public static final Pattern semver = Pattern.compile('((\\d+)\\.(\\d+)\\.(\\d+))')

	String name
	String namespace
	String version
	String description

	override validate() throws RuntimeException {
		Validate.notBlank(name)
		Validate.notBlank(namespace)
		Validate.notBlank(version)
		Validate.notBlank(description)

		val Matcher matcher = semver.matcher(version);
		Validate.validState(matcher.matches, "identity: bad version - " + version)
		true
	}

	def getQualifiedName(){
		namespace + '::' + name + '::' + version
	}

	def getVersionedName(){
		name + '::' + version
	}

	def getMajor(){
		val Matcher matcher = semver.matcher(version);
		matcher.matches
		Integer.parseInt(matcher.group(2))
	}

	def getMinor(){
		val Matcher matcher = semver.matcher(version);
		matcher.matches
		Integer.parseInt(matcher.group(3))
	}

	def getPatch(){
		val Matcher matcher = semver.matcher(version);
		matcher.matches
		Integer.parseInt(matcher.group(4))
	}
}