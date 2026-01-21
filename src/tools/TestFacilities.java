package tools;

public class TestFacilities {
    public static void main(String[] args) {
        java.util.List<model.Facility> list = model.services.FacilityService.getAllFacilities();
        for (model.Facility f : list) {
            System.out.println(f.getId() + " -> " + f.getStatus());
        }
    }
}
