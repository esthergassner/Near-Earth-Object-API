import java.util.HashMap;
import java.util.List;

public class NEOResponse
{
    HashMap<String, List<NearEarthObject>> near_earth_objects;

    public HashMap<String, List<NearEarthObject>> getNear_earth_objects()
    {
        return near_earth_objects;
    }
}
