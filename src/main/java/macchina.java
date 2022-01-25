public class macchina {

    int id_macchina;

    String brand;
    String modello;
    String condizione;
    int kilometraggio;
    int cavalli;
    int prezzo;

    public macchina() {
    }

    public macchina(int id_macchina, String brand, String modello, String condizione, int kilometraggio, int cavalli, int prezzo) {
        this.id_macchina = id_macchina;
        this.brand = brand;
        this.modello = modello;
        this.condizione = condizione;
        this.kilometraggio = kilometraggio;
        this.cavalli = cavalli;
        this.prezzo = prezzo;
    }

    public int getId() {
        return id_macchina;
    }

    public void setId(int id_macchina) {
        this.id_macchina = id_macchina;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModello() {
        return modello;
    }

    public void setModello(String modello) {
        this.modello = modello;
    }

    public String getCondizione() {
        return condizione;
    }

    public void setCondizione(String condizione) {
        this.condizione = condizione;
    }

    public int getKilometraggio() {
        return kilometraggio;
    }

    public void setKilometraggio(int kilometraggio) {
        this.kilometraggio = kilometraggio;
    }

    public int getCavalli() {
        return cavalli;
    }

    public void setCavalli(int cavalli) {
        this.cavalli = cavalli;
    }

    public int getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(int prezzo) {
        this.prezzo = prezzo;
    }

    @Override
    public String toString() {
        return "macchina{" +
                "id_macchina='" + id_macchina + '\'' +
                ", brand='" + brand + '\'' +
                ", modello='" + modello + '\'' +
                ", condizione='" + condizione + '\'' +
                ", kilometraggio=" + kilometraggio +
                ", cavalli=" + cavalli +
                ", prezzo=" + prezzo +
                '}';
    }
}

