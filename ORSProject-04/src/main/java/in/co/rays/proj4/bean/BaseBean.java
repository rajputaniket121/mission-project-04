package in.co.rays.proj4.bean;

import java.sql.Timestamp;

/**
 * BaseBean is an abstract class that provides common properties and methods
 * for all persistent beans in the application.
 *
 * <p>
 * It implements {@link DropdownListBean} to allow derived beans to be used
 * in dropdown lists with a key-value representation.
 * </p>
 *
 * <p>
 * Common properties include:
 * <ul>
 *   <li>id - Unique identifier of the bean</li>
 *   <li>createdBy - The user who created the record</li>
 *   <li>modifiedBy - The user who last modified the record</li>
 *   <li>createdDateTime - Timestamp of creation</li>
 *   <li>modifiedDateTime - Timestamp of last modification</li>
 * </ul>
 * </p>
 */
public abstract class BaseBean implements DropdownListBean {

    /** Unique identifier for the bean */
    protected long id;

    /** User who created this bean */
    protected String createdBy;

    /** User who last modified this bean */
    protected String modifiedBy;

    /** Timestamp when this bean was created */
    protected Timestamp createdDateTime;

    /** Timestamp when this bean was last modified */
    protected Timestamp modifiedDateTime;

    /** Returns the unique identifier of the bean */
    public long getId() {
        return id;
    }

    /** Sets the unique identifier of the bean */
    public void setId(long id) {
        this.id = id;
    }

    /** Returns the user who created the bean */
    public String getCreatedBy() {
        return createdBy;
    }

    /** Sets the user who created the bean */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /** Returns the user who last modified the bean */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /** Sets the user who last modified the bean */
    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /** Returns the creation timestamp of the bean */
    public Timestamp getCreatedDateTime() {
        return createdDateTime;
    }

    /** Sets the creation timestamp of the bean */
    public void setCreatedDateTime(Timestamp createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    /** Returns the last modification timestamp of the bean */
    public Timestamp getModifiedDateTime() {
        return modifiedDateTime;
    }

    /** Sets the last modification timestamp of the bean */
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
