package smack.utilities;

/**
 * The  RoutingWarning class that defines the "routing warning" object
 * 
 * @author Ammit Heeramun <ammit.heeramun@idsoft.mu>
 * @version 1.0
 */
public class RoutingWarning {
    
    private String code;
    private String decription;
    
    /**
     * Get the code of the routing warning
     * 
     * @return String which represent the routing warning code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Get the description of the routing warning
     * 
     * @return String which represent the routing warning description
     */
    public String getDecription() {
        return decription;
    }
    
    /**
     * RoutingWarning class constructor
     * 
     * @param code Code of the routing warning
     * @param description Description of the routing warning
     */
    public RoutingWarning(String code, String description) {
        
        this.code = code;
        this.decription = description;
    }
}
