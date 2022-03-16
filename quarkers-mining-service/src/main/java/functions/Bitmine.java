package functions;

public class Bitmine {

    private String type;
    private Double weight;
    private String status;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Double getWeight() {
        return weight;
    }
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Bitmine [status=" + status + ", type=" + type + ", weight=" + weight + "]";
    }

}
