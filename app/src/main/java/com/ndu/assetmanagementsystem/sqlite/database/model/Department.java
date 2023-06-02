package com.ndu.assetmanagementsystem.sqlite.database.model;

public class Department {
    public static final String TABLE_NAME_DEPT = "mDepartment";
    public static final String COLUMN_DEPT_ID = "intDepartementID";
    public static final String COLUMN_DEPT_CODE = "txtDepartementCode";
    public static final String COLUMN_DEPT_NAME = "txtDepartementName";

    private String mDepartment;
    private String txtDepartementCode;
    private String txtDepartementName;

    public String getmDepartment() {
        return mDepartment;
    }

    public void setmDepartment(String mDepartment) {
        this.mDepartment = mDepartment;
    }

    public String getTxtDepartementCode() {
        return txtDepartementCode;
    }

    public void setTxtDepartementCode(String txtDepartementCode) {
        this.txtDepartementCode = txtDepartementCode;
    }

    public String getTxtDepartementName() {
        return txtDepartementName;
    }

    public void setTxtDepartementName(String txtDepartementName) {
        this.txtDepartementName = txtDepartementName;
    }


    public Department() {
    }
}
