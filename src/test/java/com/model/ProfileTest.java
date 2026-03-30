package com.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProfileTest {

    // Bug #73 — Profile gradYear handling is inconsistent

    @Test
    public void profile_constructor_negativeGradYear_clampsToZero() {
        Profile p = new Profile("School", "CS", -5);
        assertEquals(0, p.getGradYear());
    }

    @Test
    public void profile_updateProfile_negativeGradYear_doesNotChange() {
        Profile p = new Profile("School", "CS", 2026);
        p.updateProfile(null, null, -1);
        assertEquals(2026, p.getGradYear());
    }

    @Test
    public void profile_updateProfile_zeroGradYear_setsToZero() {
        Profile p = new Profile("School", "CS", 2026);
        p.updateProfile(null, null, 0);
        assertEquals(0, p.getGradYear());
    }

    // Constructor

    @Test
    public void defaultConstructor_allDefaults() {
        Profile p = new Profile();
        assertEquals("", p.getSchool());
        assertEquals("", p.getMajor());
        assertEquals(0, p.getGradYear());
        assertEquals(0, p.getTotalUpvotes());
        assertEquals("", p.getResumeURL());
    }

    @Test
    public void threeArgConstructor_setsFields() {
        Profile p = new Profile("USC", "CS", 2026);
        assertEquals("USC", p.getSchool());
        assertEquals("CS", p.getMajor());
        assertEquals(2026, p.getGradYear());
    }

    @Test
    public void constructor_nullSchool_defaultsToEmpty() {
        assertEquals("", new Profile(null, "CS", 2026).getSchool());
    }

    @Test
    public void constructor_nullMajor_defaultsToEmpty() {
        assertEquals("", new Profile("USC", null, 2026).getMajor());
    }

    @Test
    public void constructor_negativeUpvotes_clampsToZero() {
        assertEquals(0, new Profile("USC", "CS", 2026, -10, "").getTotalUpvotes());
    }

    // updateProfile

    @Test
    public void updateProfile_validFields_updatesAll() {
        Profile p = new Profile();
        p.updateProfile("MIT", "EE", 2027);
        assertEquals("MIT", p.getSchool());
        assertEquals("EE", p.getMajor());
        assertEquals(2027, p.getGradYear());
    }

    @Test
    public void updateProfile_nullSchool_doesNotChangeSchool() {
        Profile p = new Profile("USC", "CS", 2026);
        p.updateProfile(null, "EE", 2027);
        assertEquals("USC", p.getSchool());
        assertEquals("EE", p.getMajor());
    }

    @Test
    public void updateProfile_nullMajor_doesNotChangeMajor() {
        Profile p = new Profile("USC", "CS", 2026);
        p.updateProfile("MIT", null, 2027);
        assertEquals("MIT", p.getSchool());
        assertEquals("CS", p.getMajor());
    }

    // updateUpvotes

    @Test
    public void updateUpvotes_positiveAmount_increases() {
        Profile p = new Profile();
        p.updateUpvotes(5);
        assertEquals(5, p.getTotalUpvotes());
    }

    @Test
    public void updateUpvotes_negativeAmount_clampsToZero() {
        Profile p = new Profile();
        p.updateUpvotes(3);
        p.updateUpvotes(-10);
        assertEquals(0, p.getTotalUpvotes());
    }

    // setResumeURL

    @Test
    public void setResumeURL_valid_setsUrl() {
        Profile p = new Profile();
        p.setResumeURL("https://resume.example.com/me.pdf");
        assertEquals("https://resume.example.com/me.pdf", p.getResumeURL());
    }

    @Test
    public void setResumeURL_null_setsEmpty() {
        Profile p = new Profile();
        p.setResumeURL(null);
        assertEquals("", p.getResumeURL());
    }
}
