package org.mehaexample.asdDemo.model.alignprivate;

public class Photos {
  private String neuId;
  private String type;
  private byte[] photo;

  public Photos(String neuId, String type, byte[] photo) {
    this.neuId = neuId;
    this.type = type;
    this.photo = photo;
  }

  public Photos() {}

  public String getNeuId() {
    return neuId;
  }

  public void setNeuId(String neuId) {
    this.neuId = neuId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public byte[] getPhoto() {
    return photo;
  }

  public void setPhoto(byte[] photo) {
    this.photo = photo;
  }
}
