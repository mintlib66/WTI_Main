package cnu.proejct.WTIsystem;

public class Device {
      /* timestamp: 단말 감지시간
      * apAddress: 단말 감지 AP
      * deviceMAC: 단말 MAC
      * IsCertified: 단말 상태(인가여부)
      */
      private String timestamp;
      private String apAddress;
      private String deviceMAC;
      private String IsCertified;

      //Constructor
      public Device(){
      }

      public Device(String timestamp, String apAddress, String deviceMAC, String IsCertified){
            this.timestamp = timestamp;
            this.apAddress = apAddress;
            this.deviceMAC = deviceMAC;
            this.IsCertified = IsCertified;
      }

      public String getTimestamp() {
            return timestamp;
      }

      public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
      }

      public String getApAddress() {
            return apAddress;
      }

      public void setApAddress(String apAddress) {
            this.apAddress = apAddress;
      }

      public String getDeviceMAC() {
            return deviceMAC;
      }

      public void setDeviceMAC(String deviceMAC) {
            this.deviceMAC = deviceMAC;
      }

      public String getIsCertified() {
            return IsCertified;
      }

      public void setIsCertified(String isCertified) {
            IsCertified = isCertified;
      }
}
