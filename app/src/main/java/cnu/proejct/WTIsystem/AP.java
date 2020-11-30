package cnu.proejct.WTIsystem;

public class AP {

      /* coord : AP의 위치좌표 - 맵에 표시하기 위한 값
      * device : 감지된 device들의 배열
      * info :기타 AP정보
      */

      private double[] coord;
      private Device[] device;
      private String info;

      //Constructor
      public AP(){
      }

      public AP(double[] coord, Device[] device, String info ){
            this.coord = coord;
            this.device = device;
            this.info = info;
      }

      public double[] getCoord() {
            return coord;
      }

      public void setCoord(double[] coord) {
            this.coord = coord;
      }

      public Device[] getDevice() {
            return device;
      }

      public void setDevice(Device[] device) {
            this.device = device;
      }

      public String getIsInfo() {
            return info;
      }

      public void setInfo(String info) {
            info = info;
      }
}
