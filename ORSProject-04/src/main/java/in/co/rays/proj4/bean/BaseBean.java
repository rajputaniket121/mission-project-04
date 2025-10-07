package in.co.rays.proj4.bean;

import java.sql.Timestamp;

/**
 * BaseBean is an abstract class that provides common properties and methods
 * for all persistent beans in the application.
 */
public abstract class BaseBean implements DropdownListBean {

    protected long id;

    protected String createdBy;

    protected String modifiedBy;

    protected Timestamp createdDateTime;


    protected Timestamp modifiedDateTime;


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getCreatedBy() {
        return createdBy;
    }


    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Timestamp getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Timestamp createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Timestamp getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(Timestamp modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    /**
     * Returns a string representation of the BaseBean object.
     *
     * @return a string containing all the common properties of the bean
     */
    @Override
    public String toString() {
        return "BaseBean [id=" + id + ", created_by=" + createdBy + ", modified_by=" + modifiedBy
                + ", createdDateTime=" + createdDateTime + ", modifiedDateTime=" + modifiedDateTime + "]";
    }
}
