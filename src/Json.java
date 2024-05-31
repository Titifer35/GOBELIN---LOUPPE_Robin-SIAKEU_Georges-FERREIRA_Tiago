import java.util.List;

public class Json {
	
    private List<Integer> CapacityFacility;
    private List<Integer> FixedcostFacility;
    private List<Integer> DemandCustomer;
    private int[][] CostMatrix;
    private int NumCustomers;
    private int NumFacilityLocation;

    public Json(List<Integer> CapacityFacility, List<Integer> FixedcostFacility, List<Integer> DemandCustomer, int[][] CostMatrix, int NumCustomers, int NumFacilityLocation) {
        this.CapacityFacility = CapacityFacility;
        this.FixedcostFacility = FixedcostFacility;
        this.DemandCustomer = DemandCustomer;
        this.CostMatrix = CostMatrix;
        this.NumCustomers = NumCustomers;
        this.NumFacilityLocation = NumFacilityLocation;
    }

    public List<Integer> getCapacityFacility() {
        return CapacityFacility;
    }

    public List<Integer> getFixedcostFacility() {
        return FixedcostFacility;
    }

    public List<Integer> getDemandCustomerr() {
        return DemandCustomer;
    }

    public int[][] getCostMatrix() {
        return CostMatrix;
    }

    public int getNumCustomers() {
        return NumCustomers;
    }

    public int getNumFacilityLocation() {
        return NumFacilityLocation;
    }

    public void setCapacityFacility(List<Integer> CapacityFacility) {
        this.CapacityFacility = CapacityFacility;
    }

    public void setFixedcostFacility(List<Integer> FixedcostFacility) {
        this.FixedcostFacility = FixedcostFacility;
    }

    public void setDemandCustomer(List<Integer> DemandCustomer) {
        this.DemandCustomer = DemandCustomer;
    }

    public void setCostMatrix(int[][] CostMatrix) {
        this.CostMatrix = CostMatrix;
    }

    public void setNumCustomers(int NumCustomers) {
        this.NumCustomers = NumCustomers;
    }

    public void setNumFacilityLocation(int NumFacilityLocation) {
        this.NumFacilityLocation = NumFacilityLocation;
    }
}
