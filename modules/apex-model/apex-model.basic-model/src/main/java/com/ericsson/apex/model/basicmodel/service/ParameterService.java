/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.model.basicmodel.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.ericsson.apex.model.basicmodel.concepts.ApexRuntimeException;

/**
 * The parameter service makes Apex parameters available to all classes in a JVM.
 *
 * The reason for having a parameter service is to avoid having to pass parameters down long call chains in modules such as the Apex engine and editor. The
 * parameter service makes parameters available statically.
 *
 * The parameter service must be used with care because changing a parameter set anywhere in a JVM will affect all users of those parameters anywhere in the
 * JVM.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public abstract class ParameterService {
    // The map holding the parameters
    private static Map<Class<?>, AbstractParameters> parameterMap = new ConcurrentHashMap<>();

    /**
     * This class is an abstract static class that cannot be extended.
     */
    private ParameterService() {
    }

    /**
     * Register parameters with the parameter service.
     *
     * @param <P> the generic type
     * @param parametersClass the class of the parameter, used to index the parameter
     * @param parameters the parameters
     */
    public static <P extends AbstractParameters> void registerParameters(final Class<P> parametersClass, final P parameters) {
        parameterMap.put(parametersClass, parameters);
    }

    /**
     * Remove parameters from the parameter service.
     *
     * @param <P> the generic type
     * @param parametersClass the class of the parameter, used to index the parameter
     */
    public static <P extends AbstractParameters> void deregisterParameters(final Class<P> parametersClass) {
        parameterMap.remove(parametersClass);
    }

    /**
     * Get parameters from the parameter service.
     *
     * @param <P> the generic type
     * @param parametersClass the class of the parameter, used to index the parameter
     * @return The parameter
     */
    @SuppressWarnings("unchecked")
    public static <P extends AbstractParameters> P getParameters(final Class<P> parametersClass) {
        final P parameter = (P) parameterMap.get(parametersClass);

        if (parameter == null) {
            throw new ApexRuntimeException("Parameters for " + parametersClass.getCanonicalName() + " not found in parameter service");
        }

        return parameter;
    }

    /**
     * Check if parameters is defined on the parameter service.
     *
     * @param <P> the generic type
     * @param parametersClass the class of the parameter, used to index the parameter
     * @return true if the parameter is defined
     */
    public static <P extends AbstractParameters> boolean existsParameters(final Class<P> parametersClass) {
        return parameterMap.get(parametersClass) != null;
    }

    /**
     * Get all the entries in the parameters map.
     *
     * @return The entries
     */
    public static Set<Entry<Class<?>, AbstractParameters>> getAll() {
        return parameterMap.entrySet();
    }

    /**
     * Clear all parameters in the parameter service.
     */
    public static void clear() {
        parameterMap.clear();
    }
}
