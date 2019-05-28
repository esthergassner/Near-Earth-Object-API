import java.util.HashMap;
import java.util.List;

public class NEOResponse
{
    private HashMap<String, List<NearEarthObject>> near_earth_objects;
    private int element_count;

    public HashMap<String, List<NearEarthObject>> getNear_earth_objects() {return near_earth_objects;}

    public int getElement_count() {return element_count;}
}
