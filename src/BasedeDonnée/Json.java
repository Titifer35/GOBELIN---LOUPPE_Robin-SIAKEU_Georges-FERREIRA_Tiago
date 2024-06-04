package BasedeDonnée;

import java.util.List;

public class Json {
	
    private List<Integer> capacity_facility;
    private List<Integer> fixed_cost_facility;
    private List<Integer> demand_customer;
    private int[][] cost_matrix;
    private int num_customers;
    private int num_facility_locations;

    public Json(List<Integer> capacity_facility, List<Integer> fixed_cost_facility, List<Integer> demand_customer, int[][] cost_matrix, int num_customers, int num_facility_locations) {
        this.capacity_facility = capacity_facility;
        this.fixed_cost_facility = fixed_cost_facility;
        this.demand_customer = demand_customer;
        this.cost_matrix = cost_matrix;
        this.num_customers = num_customers;
        this.num_facility_locations = num_facility_locations;
    }

    public List<Integer> getCapacityFacility() {
        return capacity_facility;
    }

    public List<Integer> getFixedcostFacility() {
        return fixed_cost_facility;
    }

    public List<Integer> getDemandCustomerr() {
        return demand_customer;
    }

    public int[][] getCostMatrix() {
        return cost_matrix;
    }

    public int getNumCustomers() {
        return num_customers;
    }

    public int getNumFacilityLocation() {
        return num_facility_locations;
    }

    public void setCapacityFacility(List<Integer> capacity_facility) {
        this.capacity_facility = capacity_facility;
    }

    public void setFixedcostFacility(List<Integer> fixed_cost_facility) {
        this.fixed_cost_facility = fixed_cost_facility;
    }

    public void setDemandCustomer(List<Integer> demand_customer) {
        this.demand_customer = demand_customer;
    }

    public void setCostMatrix(int[][] cost_matrix) {
        this.cost_matrix = cost_matrix;
    }

    public void setNumCustomers(int num_customers) {
        this.num_customers = num_customers;
    }

    public void setNumFacilityLocation(int num_facility_locations) {
        this.num_facility_locations = num_facility_locations;
    }
}
