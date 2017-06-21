package aibt.will.com.myaibt.util;


import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;
import android.util.Log;

/**
 * @author 王晶
 * @date 17-5-24-上午9:30
 * @desc
 */
public final class BluetoothDeviceFilter {

    private static final String TAG = "BluetoothDeviceFilter";

    /** The filter interface to external classes. */
    public interface Filter {
        boolean matches(BluetoothDevice device);
    }

    /** All filter singleton (referenced directly). */
    public static final Filter ALL_FILTER = new AllFilter();
    /** Bonded devices only filter (referenced directly). */
    public static final Filter BONDED_DEVICE_FILTER = new BondedDeviceFilter();
    /** Unbonded devices only filter (referenced directly). */
    public static final Filter UNBONDED_DEVICE_FILTER = new UnbondedDeviceFilter();
    /** Table of singleton filter objects. */
    private static final Filter[] FILTERS = { ALL_FILTER, // FILTER_TYPE_ALL
            new AudioFilter(), // FILTER_TYPE_AUDIO
            new TransferFilter(), // FILTER_TYPE_TRANSFER
            new PanuFilter(), // FILTER_TYPE_PANU
            new NapFilter(), // FILTER_TYPE_NAP
            new HidFilter() // FILTER_TYPE_HID
    };
    public static final int FILTER_TYPE_ALL = 0;
    public static final int FILTER_TYPE_AUDIO = 1;
    public static final int FILTER_TYPE_TRANSFER = 2;
    public static final int FILTER_TYPE_PANU = 3;
    public static final int FILTER_TYPE_NAP = 4;
    public static final int FILTER_TYPE_HID = 5;

    /** Private constructor. */
    private BluetoothDeviceFilter() {
    }

    /**
     * Returns the singleton {@link Filter} object for the specified type, or
     * {@link #ALL_FILTER} if the type value is out of range.
     *
     * @param filterType
     *            a constant from BluetoothDevicePicker
     * @return a singleton object implementing the {@link Filter} interface.
     */
    public static Filter getFilter(int filterType) {
        if (filterType >= 0 && filterType < FILTERS.length) {
            return FILTERS[filterType];
        } else {
            Log.w(TAG, "Invalid filter type " + filterType
                    + " for device picker");
            return ALL_FILTER;
        }
    }

    /** Filter that matches all devices. */
    private static final class AllFilter implements Filter {
        public boolean matches(BluetoothDevice device) {
            return true;
        }
    }

    /** Filter that matches only bonded devices. */
    private static final class BondedDeviceFilter implements Filter {
        public boolean matches(BluetoothDevice device) {
            return device.getBondState() == BluetoothDevice.BOND_BONDED;
        }
    }

    /** Filter that matches only unbonded devices. */
    private static final class UnbondedDeviceFilter implements Filter {
        public boolean matches(BluetoothDevice device) {
            return device.getBondState() != BluetoothDevice.BOND_BONDED;
        }
    }

    /** Parent class of filters based on UUID and/or Bluetooth class. */
    private abstract static class ClassUuidFilter implements Filter {
        abstract boolean matches(ParcelUuid[] uuids, BluetoothClass btClass);

        public boolean matches(BluetoothDevice device) {
            if(device == null){
                return false;
            }
            return matches(device.getUuids(), device.getBluetoothClass());
        }
    }

    /** Filter that matches devices that support AUDIO profiles. */
    private static final class AudioFilter extends ClassUuidFilter {
        @Override
        boolean matches(ParcelUuid[] uuids, BluetoothClass btClass) {
            if (uuids != null) {
                if (BluetoothUuid
                        .containsAnyUuid(uuids, A2dpProfileConst.SINK_UUIDS)) {
                    return true;
                }
                if (BluetoothUuid.containsAnyUuid(uuids, HeadsetProfileConst.UUIDS)) {
                    return true;
                }
            } else if (btClass != null) {
//                if (btClass.doesClassMatch(BluetoothClass.PROFILE_A2DP)
//                        || btClass
//                        .doesClassMatch(BluetoothClass.PROFILE_HEADSET)) {
//                    return true;
//                }
            }
            return false;
        }
    }

    /** Filter that matches devices that support Object Transfer. */
    private static final class TransferFilter extends ClassUuidFilter {
        @Override
        boolean matches(ParcelUuid[] uuids, BluetoothClass btClass) {
            if (uuids != null) {
                if (BluetoothUuid.isUuidPresent(uuids,
                        BluetoothUuid.ObexObjectPush)) {
                    return true;
                }
            }
//            return btClass != null
//                    && btClass.doesClassMatch(BluetoothClass.PROFILE_OPP);
            return true;
        }
    }

    /** Filter that matches devices that support PAN User (PANU) profile. */
    private static final class PanuFilter extends ClassUuidFilter {
        @Override
        boolean matches(ParcelUuid[] uuids, BluetoothClass btClass) {
            if (uuids != null) {
                if (BluetoothUuid.isUuidPresent(uuids, BluetoothUuid.PANU)) {
                    return true;
                }
            }
//            return btClass != null
//                    && btClass.doesClassMatch(BluetoothClass.PROFILE_PANU);
            return true;
        }
    }

    /** Filter that matches devices that support NAP profile. */
    private static final class NapFilter extends ClassUuidFilter {
        @Override
        boolean matches(ParcelUuid[] uuids, BluetoothClass btClass) {
            if (uuids != null) {
                if (BluetoothUuid.isUuidPresent(uuids, BluetoothUuid.NAP)) {
                    return true;
                }
            }
//            return btClass != null
//                    && btClass.doesClassMatch(BluetoothClass.PROFILE_NAP);
            return true;
        }
    }

    /** Filter that matches devices that support HID profile. */
    private static final class HidFilter extends ClassUuidFilter {
        @Override
        boolean matches(ParcelUuid[] uuids, BluetoothClass btClass) {
            if (uuids != null) {
                if (BluetoothUuid.isUuidPresent(uuids, BluetoothUuid.Hid)) {
                    return true;
                }
            }
//            return btClass != null
//                    && btClass.doesClassMatch(BluetoothClass.PROFILE_HID);
            return true;
        }
    }
}
