package com.ndu.assetmanagementsystem;

public class NandurLibs {

    public static String versName;
    public static int versCode;

    // --Commented out by Inspection START (14-Jan-21 15:25):
//    /**
//     * Get external sd card path using reflection
//     *
//     * @param mContext
//     * @param is_removable is external storage removable
//     * @return
//     */
//    @SuppressWarnings("JavaDoc")
//    private static String getExternalStoragePath(Context mContext, boolean is_removable) {
//
//        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
//        Class<?> storageVolumeClazz;
//        try {
//            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
//            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
//            @SuppressWarnings("JavaReflectionMemberAccess") Method getPath = storageVolumeClazz.getMethod("getPath");
//            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
//            Object result = getVolumeList.invoke(mStorageManager);
//            final int length = Array.getLength(Objects.requireNonNull(result));
//            for (int i = 0; i < length; i++) {
//                Object storageVolumeElement = Array.get(result, i);
//                String path = (String) getPath.invoke(storageVolumeElement);
//                //noinspection ConstantConditions
//                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
//                if (is_removable == removable) {
//                    return path;
//                }
//            }
//        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
// --Commented out by Inspection STOP (14-Jan-21 15:25)

}
