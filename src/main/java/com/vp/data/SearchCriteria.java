package com.vp.data;

public class SearchCriteria {
    public enum Sort {
        ASC, DESC
    }

    private String city;
    private Sort sort;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city =city;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(String sort) {
        Sort pSort;
        try {
            pSort = Sort.valueOf(sort.toUpperCase());
        } catch (Exception e) {
            pSort = Sort.ASC;
        }
        this.sort = pSort;
    }

}
