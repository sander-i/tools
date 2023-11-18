package org.tools.bedrock.util;

import org.junit.Test;
import org.tools.bedrock.base.Address;

import java.util.ArrayList;
import java.util.List;

public class ListUtilsTest {

    @Test
    public void arrayListReversal() {
    }

    @Test
    public void arrayListReversalStream() {
    }

    @Test
    public void extractGenericList() {
    }

    @Test
    public void extractGenericListStream() {
    }

    @Test
    public void pagination() {
        List<Address> addressList = new ArrayList<>();
        addressList.add(new Address().setRemark("1"));
        addressList.add(new Address().setRemark("2"));
        addressList.add(new Address().setRemark("3"));
        addressList.add(new Address().setRemark("4"));
        addressList.add(new Address().setRemark("5"));
        addressList.add(new Address().setRemark("6"));
        addressList.add(new Address().setRemark("7"));
        addressList.add(new Address().setRemark("8"));
        addressList.add(new Address().setRemark("9"));

        addressList.add(new Address().setRemark("1"));
        addressList.add(new Address().setRemark("2"));
        addressList.add(new Address().setRemark("3"));
        addressList.add(new Address().setRemark("4"));
        addressList.add(new Address().setRemark("5"));
        addressList.add(new Address().setRemark("6"));
        addressList.add(new Address().setRemark("7"));
        addressList.add(new Address().setRemark("8"));
        addressList.add(new Address().setRemark("9"));

        List<List<Address>> pagination = ListUtils.pagination(addressList, 5);
        System.out.println(pagination);
    }
}