package com.priyankaj.doctorsapp.model;


import java.util.ArrayList;

public class AboutDetails {

    public ArrayList<AboutDetails.AboutUs> getAboutUs() {
        return Aboutus;
    }

    public void setAboutUs(ArrayList<AboutDetails.AboutUs> aboutUs) {
        Aboutus = aboutUs;
    }

    private ArrayList<AboutUs> Aboutus;

    public class AboutUs{
        private String did;
        private String category_id;
        private String qualification;
        private String about_text;
        private String name;
        private String email;
        private String mobile;
        private String detail;
        private String status;
        private String regdate;

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getQualification() {
            return qualification;
        }

        public void setQualification(String qualification) {
            this.qualification = qualification;
        }

        public String getAbout() {
            return about_text;
        }

        public void setAbout(String about) {
            this.about_text = about;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRegdate() {
            return regdate;
        }

        public void setRegdate(String regdate) {
            this.regdate = regdate;
        }
    }

}