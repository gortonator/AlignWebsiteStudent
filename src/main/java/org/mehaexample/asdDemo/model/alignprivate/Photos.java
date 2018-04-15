package org.mehaexample.asdDemo.model.alignprivate;

public class Photos {
  private String neuId;
  private byte[] photo;

  public Photos(String neuId, byte[] photo) {
    this.neuId = neuId;
    this.photo = photo;
  }

  public Photos() {}

  public String getNeuId() {
    return neuId;
  }

  public void setNeuId(String neuId) {
    this.neuId = neuId;
  }

  public byte[] getPhoto() {
    return photo;
  }

  public void setPhoto(byte[] photo) {
    this.photo = photo;
  }
}
