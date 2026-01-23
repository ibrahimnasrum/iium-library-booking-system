public class RunModelTest {
    public static void main(String[] args) {
        java.util.List<model.Facility> list = model.services.FacilityService.getAllFacilities();
        System.out.println("Total facilities: " + list.size());
        for (model.Facility f : list) {
            System.out.println(f.getId() + " -> " + f.getStatus());
        }
    }
}