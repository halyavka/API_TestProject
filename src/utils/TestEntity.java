package utils;

public class TestEntity {

    public Player player;
    public TestHttpClient client;
    public StringBuilder stringBuilder;

    public TestEntity(TestHttpClient client) {
        this.stringBuilder = new StringBuilder();
        this.client = client;
    }

}

