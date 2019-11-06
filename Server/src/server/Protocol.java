package server;

import java.io.*;

public class Protocol implements Serializable {
	public static final int PT_UNDEFINED = -1;
    public static final int PT_EXIT = 0;
    public static final int PT_REQ_LOGIN = 1;
    public static final int PT_RES_LOGIN = 2;
    public static final int PT_LOGIN_RESULT = 3;
    public static final int PT_Ventilator_ORDER = 4;
    public static final int PT_USERHUM_SET = 5;
    public static final int LEN_LOGIN_ID = 20;
    public static final int LEN_LOGIN_PASSWORD = 20;
    public static final int LEN_LOGIN_RESULT = 2;
    public static final int LEN_PROTOCOL_TYPE = 1;
    public static final int LEN_MAX = 1000;
    public static final int LEN_Ventilator_ORDER = 20;
    public static final int LEN_USERHUM_SET = 50;

    public static final int PT_ID_ORDER = 6;
    public static final int LEN_TEMPERATURE = 10;   // 온도
    public static final int LEN_HUMIDITY = 10;      // 습도
    public static final int PT_REQ_DEV_STATUS = 7;  // 장치 상태 요청 프로토콜
    public static final int PT_RES_DEV_STATUS = 8;  // 장치 상태 응답 프로토콜


    public static final int PT_RES_FAN = 10;
    public static final int LEN_RES_FAN = 20;

    public static final int PT_RESFAN_RESULT = 11;
    public static final int LEN_RES_FAN_RESULT = 20;

    public static final int PT_TOML = 12;
    public static final int LEN_TOML = 50;
    
    public static final int PT_AUTO_ON_OFF = 13;
    public static final int LEN_AUTO_ON_OFF = 50;

    protected int protocolType;
    private byte[] packet;

    int temperature;
    int humidity;



    public Protocol() {
        this(PT_UNDEFINED);
    }

    public Protocol(int protocolType) {
        this.protocolType = protocolType;
        getPacket(protocolType);
    }

    public byte[] getPacket(int protocolType) {
        if (packet == null) {
            switch (protocolType) {
            case PT_AUTO_ON_OFF:
                packet = new byte[LEN_PROTOCOL_TYPE + LEN_AUTO_ON_OFF];
                break;
                case PT_REQ_LOGIN:
                    packet = new byte[LEN_PROTOCOL_TYPE];
                    break;
                case PT_RES_LOGIN:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_LOGIN_PASSWORD];
                    break;
                case PT_UNDEFINED:
                    packet = new byte[LEN_MAX];
                    break;
                case PT_LOGIN_RESULT:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_RESULT];
                    break;
                case PT_EXIT:
                    packet = new byte[LEN_PROTOCOL_TYPE];
                    break;
                case PT_Ventilator_ORDER:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_Ventilator_ORDER];
                    break;
                case PT_USERHUM_SET:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_USERHUM_SET];
                    break;
                case PT_ID_ORDER:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_Ventilator_ORDER];
                    break;
                case PT_REQ_DEV_STATUS: // 추가
                    packet = new byte[LEN_PROTOCOL_TYPE];
                    break;
                case PT_RES_DEV_STATUS:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_TEMPERATURE + LEN_HUMIDITY + LEN_Ventilator_ORDER];
                    break;
                case PT_RES_FAN:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_RES_FAN + LEN_LOGIN_ID];
                    break;
                case PT_RESFAN_RESULT:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_RES_FAN_RESULT];
                    break;
                case PT_TOML:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_Ventilator_ORDER];
                    break;
            }
        }
        packet[0] = (byte) protocolType;
        return packet;
    }


    public String getLoginResult() {
        return new String(packet, LEN_PROTOCOL_TYPE, LEN_LOGIN_RESULT).trim();
    }

    public void setLoginResult(String ok) {
        System.arraycopy(ok.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, ok.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE + ok.trim().getBytes().length] = '\0';
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }

    public int getProtocolType() {
        return protocolType;
    }

    public byte[] getPacket() {
        return packet;
    }

    public void setPacket(int pt, byte[] buf) {
        packet = null;
        packet = getPacket(pt);
        protocolType=pt;
        System.arraycopy(buf, 0, packet, 0, packet.length);
    }

    public String getAutoOnOff() {
        return new String(packet, LEN_PROTOCOL_TYPE, LEN_AUTO_ON_OFF).trim();
    }

    public void setAutoOnOff(String order) {
        System.arraycopy(order.trim().getBytes(),0 , packet, LEN_PROTOCOL_TYPE, order.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE+order.trim().getBytes().length]='\0';
    }
    
    public String getUserSetHum() {
        return new String(packet, LEN_PROTOCOL_TYPE, LEN_USERHUM_SET).trim();
    }

    public void setUserSetHum(String sethum) {
        System.arraycopy(sethum.trim().getBytes(),0 , packet, LEN_PROTOCOL_TYPE, sethum.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE+sethum.trim().getBytes().length]='\0';
    }

    public String getOrder() {
        return new String(packet, LEN_PROTOCOL_TYPE+LEN_TEMPERATURE+LEN_HUMIDITY, LEN_Ventilator_ORDER).trim();
    }

    public void setOrder(String order) {
        System.arraycopy(order.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE+LEN_TEMPERATURE+LEN_HUMIDITY, order.getBytes().length);
        packet[LEN_PROTOCOL_TYPE+order.getBytes().length] = '\0';
    }  

    public String getUserOrder() {
        return new String(packet, LEN_PROTOCOL_TYPE, LEN_Ventilator_ORDER).trim();
    }

    public void setUserOrder(String order) {
        System.arraycopy(order.trim().getBytes(),0 , packet, LEN_PROTOCOL_TYPE, order.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE+order.trim().getBytes().length]='\0';
    }

    public String getId() {
        return new String(packet, LEN_PROTOCOL_TYPE, LEN_LOGIN_ID).trim();
    }
    
    public void setId(String id) {
        System.arraycopy(id.trim().getBytes(),0 , packet, LEN_PROTOCOL_TYPE, id.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE+id.trim().getBytes().length]='\0';
    }

    public String getPassword() {
        return new String(packet, LEN_PROTOCOL_TYPE+LEN_LOGIN_ID,LEN_LOGIN_PASSWORD).trim();
    }

    public void setPassword(String  password) {
        System.arraycopy(password.trim().getBytes(), 0, packet,LEN_PROTOCOL_TYPE+LEN_LOGIN_ID,
                password.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE+LEN_LOGIN_ID+password.trim().getBytes().length] = '\0';
    }

    public String getMyId() {
        return new String(packet, LEN_PROTOCOL_TYPE, LEN_LOGIN_ID).trim();
    }

    public void setMyId(String id) {
        System.arraycopy(id.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, id.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE + id.trim().getBytes().length] = '\0';
    }
    public String getMyOrder() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, LEN_Ventilator_ORDER).trim();
    }

    public void setMyOrder(String order) {
        System.arraycopy(order.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID,
                order.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + order.trim().getBytes().length] = '\0';
    }

    public int getTemperature() {			//먼저
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        packet[1]=(byte)temperature;
    }

    public int getHumidity() {		//order
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
        packet[2]=(byte)humidity;
    }

    public String getResfan() {
        return new String(packet, LEN_PROTOCOL_TYPE, LEN_RES_FAN).trim();
    }

    public void setResfan(String resfan) {
        System.arraycopy(resfan.trim().getBytes(),0 , packet, LEN_PROTOCOL_TYPE, resfan.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE+resfan.trim().getBytes().length]='\0';
    }

    public String getResId() {
        return new String(packet, LEN_PROTOCOL_TYPE+LEN_RES_FAN,LEN_LOGIN_ID).trim();
    }

    public void setResId(String  password) {
        System.arraycopy(password.trim().getBytes(), 0, packet,LEN_PROTOCOL_TYPE+LEN_RES_FAN,
                password.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE+LEN_RES_FAN+password.trim().getBytes().length] = '\0';
    }

    public String getResfanResult() {
        return new String(packet, LEN_PROTOCOL_TYPE, LEN_RES_FAN_RESULT).trim();
    }

    public void setResfanResult(String ok) {
        System.arraycopy(ok.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, ok.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE + ok.trim().getBytes().length] = '\0';
    }

    public String getUMLId() {
        return new String(packet, LEN_PROTOCOL_TYPE, LEN_LOGIN_ID).trim();
    }

    public void setUMLId(String id) {
        System.arraycopy(id.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE, id.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE + id.trim().getBytes().length] = '\0';
    }

    public String getUMLOrder() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, LEN_Ventilator_ORDER).trim();
    }

    public void setUMLOrder(String order) {
        System.arraycopy(order.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID,
                order.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + order.trim().getBytes().length] = '\0';
    }
}