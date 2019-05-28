import java.util.List;

public class NearEarthObject
{
    //fields
    private String id;
    private String name;
    private boolean is_potentially_hazardous_asteroid;
    private Diameter estimated_diameter;
    private List<CloseApproachData> close_approach_data;

    //methods
    String getId()
    {
        return id;
    }
    String getName() { return name; }
    public void setName(String name) {this.name = name;}
    boolean isPotentiallyHazardous() { return is_potentially_hazardous_asteroid;}
    Diameter getEstimated_diameter() {return estimated_diameter;}
    List<CloseApproachData> getClose_approach_data() {return close_approach_data;}

    //inner class
    public class Diameter
    {
        //fields
        private Feet feet;

        //methods
        Feet getFeet() {return feet;}

        //inner class
        class Feet
        {
            //fields
            private double estimated_diameter_min, estimated_diameter_max;

            //methods
            double getEstimated_diameter_min() {return estimated_diameter_min;}
            double getEstimated_diameter_max() {return estimated_diameter_max;}
        }
    }

    //inner class
    class CloseApproachData
    {
        //fields
        private String close_approach_date_full, close_approach_date;
        private Velocity relative_velocity;
        private MissDistance miss_distance;

        //methods
        String getClose_approach_date() { return close_approach_date;}
        String getClose_approach_date_full() { return close_approach_date_full;}
        Velocity getRelative_velocity() { return relative_velocity;}
        MissDistance getMiss_distance()  { return miss_distance;}

        //inner class
        class Velocity
        {
            //fields
            private String miles_per_hour;

            //methods
            String getMiles_per_hour() { return miles_per_hour;}
        }

        //inner class
        class MissDistance
        {
            //fields
            private String miles;

            //methods
            String getMiles() {return miles;}
        }
    }
}
