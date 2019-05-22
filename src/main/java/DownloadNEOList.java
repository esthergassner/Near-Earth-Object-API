import io.reactivex.disposables.Disposable;

public class DownloadNEOList
{
    public static void main(String[] args)
    {
        NasaClient client = new NasaClient();

        Disposable disposable = client.getObject("2015-09-09", "2015-09-10")
                .subscribe(System.out::println, Throwable::printStackTrace);
    }
}
