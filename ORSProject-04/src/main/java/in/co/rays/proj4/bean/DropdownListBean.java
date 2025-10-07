package in.co.rays.proj4.bean;

/**
 * DropdownListBean is a simple interface to provide a key-value pair
 * structure for populating dropdown lists in the UI.
 *
 * <p>
 * Implementing classes must provide methods to return a key (usually an
 * identifier) and a value (usually a display string) for the dropdown option.
 * </p>
 *
 * <p>
 * This interface is useful for dynamically populating HTML select elements
 * from beans, maps, or database records.
 * </p>
 */
public interface DropdownListBean {

    /**
     * Returns the unique key for the dropdown option.
     *
     * @return the key as a String
     */
    public String getKey();

    /**
     * Returns the display value for the dropdown option.
     *
     * @return the value as a String
     */
    public String getValue();
}
