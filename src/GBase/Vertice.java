package GBase;

public class Vertice {

    private int id;

    public Vertice(int id) {

        this.id = id;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    @Override
    public int hashCode() {

        return id;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vertice other = (Vertice) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
