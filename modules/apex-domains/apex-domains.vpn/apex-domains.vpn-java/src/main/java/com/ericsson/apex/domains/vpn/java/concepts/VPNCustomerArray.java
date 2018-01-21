/*******************************************************************************
 * COPYRIGHT (C) Ericsson 2016-2018
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.apex.domains.vpn.java.concepts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class to wrap a customer map to allow for schema generation of events using this type.
 *
 * @author Liam Fallon (liam.fallon@ericsson.com)
 */
public class VPNCustomerArray implements Serializable {
    private static final long serialVersionUID = 7074880898025239073L;

    private final List<String> customers = new ArrayList<>();

    /**
     * The Default Constructor of the customer array.
     */
    public VPNCustomerArray() {
    }

    /**
     * The Constructor of the customer array to initialize the customer array.
     *
     * @param newCustomers the new customers to add to the customer array
     */
    public VPNCustomerArray(final Collection<String> newCustomers) {
        customers.addAll(newCustomers);
    }

    /**
     * Adds a customer.
     *
     * @param customer the customer to add
     */
    public void addCustomer(final String customer) {
        customers.add(customer);
    }

    /**
     * Gets the customers in the customer array.
     *
     * @return the customers in the customer array
     */
    public Collection<String> getCustomers() {
        return customers;
    }

    /**
     * Removes all the customers from the customer array.
     *
     * @param removedCustomers the removed customers
     */
    public void removeAll(final Collection<String> removedCustomers) {
        customers.removeAll(removedCustomers);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return customers.toString();
    }
}
