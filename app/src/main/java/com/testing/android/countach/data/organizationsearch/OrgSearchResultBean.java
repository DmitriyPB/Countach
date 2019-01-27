package com.testing.android.countach.data.organizationsearch;

import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.domain.Organization;
import com.testing.android.countach.domain.organizationsearch.OrgSearchResult;

import java.util.List;


final public class OrgSearchResultBean implements OrgSearchResult {

    private final Organization org;
    private final List<Contact> contacts;

    public OrgSearchResultBean(Organization org, List<Contact> contacts) {
        this.org = org;
        this.contacts = contacts;
    }

    @Override
    public Organization getOrg() {
        return org;
    }

    @Override
    public List<Contact> getNearestContacts() {
        return contacts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrgSearchResultBean)) return false;

        OrgSearchResultBean that = (OrgSearchResultBean) o;

        if (getOrg() != null ? !getOrg().equals(that.getOrg()) : that.getOrg() != null)
            return false;
        return contacts != null ? contacts.equals(that.contacts) : that.contacts == null;
    }

    @Override
    public int hashCode() {
        int result = getOrg() != null ? getOrg().hashCode() : 0;
        result = 31 * result + (contacts != null ? contacts.hashCode() : 0);
        return result;
    }
}
