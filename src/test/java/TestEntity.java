import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.DecimalFormat;

public class TestEntity {
    private String name;
    private Data data;

    public TestEntity(String name, Data data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
