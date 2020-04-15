package com.project.pan.backstage;

public class PanController {

    public interface PanControllerListener {
        void onModeChanged(int mode, int val);
        // or when data has been loaded
        void onPowerValueCallback(int power);
        void onPowerValueMaxCallback(int powerMax);
        void onTubeCallback(double tube);
        void onTubeMaxCallback(int tubeMax);
        void onPlateCallback(double plate);
        void onPlateMaxCallback(int plateMax);
        void onTriacCallback(double triac);
        void onTriacMaxCallback(int triacMax);
        void onGapMaxCallback(int gapMax);
        void onPIDModeCallback(int mode);
        void onKPCallback(double kp);
        void onOverheatCallback(int overheat);
        void onOtherDataCallback(String label, String data);
        void onSendRequest(String dataOut);
    }



    public static final int MODE_IDLE = 0;
    public static final int MODE_POWER = 1;
    public static final int MODE_TEMP = 2;
    public static final int OVERHEAT_NO = 0;
    public static final int OVERHEAT_TUBE = 1;
    public static final int OVERHEAT_TRIAC = 2;
    public static final int OVERHEAT_TUBE_PLATE = 4;
    public static final int POWER_NO = 0;
    public static final int POWER_MAX = 15625;
    public static final int POWER_MED = POWER_MAX / 2;
    public static final int POWER_MIN = POWER_MAX / 4;
    public static final double PLATE_MIN = 0;
    public static final double PLATE_MAX = 200.0;
    private int mode = -1;
    private int modeValue = -1;
    private int targetMode = -1;
    private int targetModeValue = 0;
    private int overheat = -1;
    private int targetTRIACMax = -1;
    private int targetTubeMax = -1;
    private int targetGapMax = -1;
    private double targetKP = -1;

    private PanControllerListener listener;

    public PanController() {
        this.listener = null;
    }

    public void setPanControllerListener(PanControllerListener listener) {
        this.listener = listener;
    }

    public void setPlateTemp(double temp) {
        Double temp10 = temp * 10.0;
        int temp10Int = temp10.intValue();

        String dataOut = genCommand("l", temp10Int);
        listener.onSendRequest(dataOut);
        targetMode = MODE_TEMP;
        targetModeValue = temp10Int;
    }

    public void setPower(int power) {
        String dataOut = genCommand("p", power);
        listener.onSendRequest(dataOut);
        targetMode = (power == 0 ? MODE_IDLE : MODE_POWER);
        targetModeValue = power;
    }

    public void setTriacMax(int triacMax) {
        String dataOut = genCommand("r", triacMax*10);
        listener.onSendRequest(dataOut);
        targetTRIACMax = triacMax;
    }

    public void setTubeMax(int tubeMax) {
        String dataOut = genCommand("u", tubeMax*10);
        listener.onSendRequest(dataOut);
        targetTubeMax = tubeMax;
    }

    public void setGapMax(int gapMax) {
        String dataOut = genCommand("d", gapMax*10);
        listener.onSendRequest(dataOut);
        targetGapMax = gapMax;
    }

    public void setKP(double kp) {
        String dataOut = genCommand("k", (int)(kp*100));
        listener.onSendRequest(dataOut);
        targetKP = kp;
    }

    String genCommand(String tag, int val) {
        return String.format("%c%c%c", tag.charAt(0), val%128, val/128);
    }

    public void stop() {
        setPower(0);
    }

    public boolean dataIn(String dataIn) {
        boolean isValidDatain = false;
        String[] lines = dataIn.split("\r");
        for(String line : lines) {
            String[] substrings = line.split("\t");
            if(substrings.length >= 2) {
                isValidDatain = true;
                String valString = substrings[1];
                switch (substrings[0]) {
                    case "power":
                        listener.onPowerValueCallback(Integer.parseInt(valString));
                        break;
                    case "tube":
                        listener.onTubeCallback(Double.parseDouble(valString) / 10.0);
                        break;
                    case "plate":
                        listener.onPlateCallback(Double.parseDouble(valString) / 10.0);
                        break;
                    case "triac":
                        listener.onTriacCallback(Double.parseDouble(valString) / 10.0);
                        break;
                    case "pom":
                        listener.onPowerValueMaxCallback(Integer.parseInt(valString));
                        break;
                    case "trm":
                        listener.onTriacMaxCallback(Integer.parseInt(valString) / 10);
                        if(targetTRIACMax != -1) {
                            if(targetTRIACMax != Integer.parseInt(valString) / 10) {
                                setTriacMax(targetTRIACMax);
                            } else {
                                targetTRIACMax = -1;
                            }
                        }
                        break;
                    case "tum":
                        listener.onTubeMaxCallback(Integer.parseInt(valString) / 10);
                        if(targetTubeMax != -1) {
                            if(targetTubeMax != Integer.parseInt(valString) / 10) {
                                setTubeMax(targetTubeMax);
                            } else {
                                targetTubeMax = -1;
                            }
                        }
                        break;
                    case "plm":
                        listener.onPlateMaxCallback(Integer.parseInt(valString) / 10);
                        break;
                    case "urdm":
                        listener.onGapMaxCallback(Integer.parseInt(valString) / 10);
                        if(targetGapMax != -1) {
                            if(targetGapMax != Integer.parseInt(valString) / 10) {
                                setGapMax(targetGapMax);
                            } else {
                                targetGapMax = -1;
                            }
                        }
                        break;
                    case "overheat":
                        int newOverheat = Integer.parseInt(valString);
                        if(overheat != newOverheat) {
                            overheat = newOverheat;
                            listener.onOverheatCallback(overheat);
                        }
                        break;
                    case "pid_mode":
                        listener.onPIDModeCallback(Integer.parseInt(valString));
                        break;
                    case "kp_100":
                        listener.onKPCallback(Double.parseDouble(valString) / 100.0);
                        if(targetKP != -1) {
                            if(targetKP != Double.parseDouble(valString) / 100.0) {
                                setKP(targetKP);
                            } else {
                                targetKP = -1;
                            }
                        }
                        break;
                    case "mode":
                        int newMode = Integer.parseInt(valString);
                        int newModeValue = Integer.parseInt(substrings[2]);
                        if((mode != newMode) || (modeValue != newModeValue)) {
                            mode = newMode;
                            modeValue = newModeValue;
                            listener.onModeChanged(mode, modeValue);
                        }
                        if((newMode != targetMode) || (newModeValue != targetModeValue)) {
                            switch (targetMode) {
                                case MODE_IDLE:
                                    setPower(targetModeValue);
                                    break;
                                case MODE_POWER:
                                    setPower(targetModeValue);
                                    break;
                                case MODE_TEMP:
                                    setPlateTemp(((double)targetModeValue)/10);
                                    break;
                            }
                        }
                        break;
                    default:
                        listener.onOtherDataCallback(substrings[0], valString);
                        break;
                }
            }
        }
        return isValidDatain;
    }
}
