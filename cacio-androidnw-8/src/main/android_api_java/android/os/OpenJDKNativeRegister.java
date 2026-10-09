package android.os;
import java.io.*;

public class OpenJDKNativeRegister
{
	private static final String[] nativeClassArr;
	
	static {
		// This not work, so try locate in LD_LIBRARY_PATH
		// System.loadLibrary("binexecutor");
		
		File currLibFile;
		for (String ldLib : System.getenv("LD_LIBRARY_PATH").split(":")) {
			if (ldLib.isEmpty()) continue;
			currLibFile = new File(ldLib, "libpojavexec.so");
			if (currLibFile.exists()) {
				System.load(currLibFile.getAbsolutePath());
				break;
			}
		}

		nativeClassArr = new String[]{
// 00083528 T 
			"_ZN7android25register_android_util_LogEP7_JNIEnv",
// 000859dc T 
			"_ZN7android32register_android_graphics_CanvasEP7_JNIEnv",
// 0008d950 T 
			"_ZN7android30register_android_graphics_PathEP7_JNIEnv",
// 00071654 T 
			// "_ZN7android29register_android_view_SurfaceEP7_JNIEnv",
			
/*
// 00082414 T 
			"_Z26register_android_os_BinderP7_JNIEnv",
// 00084908 T 
			"_Z27register_android_os_ProcessP7_JNIEnv",
// 0008b134 T 
			"_Z31register_android_graphics_MovieP7_JNIEnv",
// 00083674 T 
			"_Z31register_android_util_FloatMathP7_JNIEnv",
// 000870c8 T 
			"_Z32register_android_graphics_BitmapP7_JNIEnv",
// 0008855c T 
			"_Z32register_android_graphics_CameraP7_JNIEnv",
// 0008f81c T 
			"_Z32register_android_graphics_ShaderP7_JNIEnv",
// 00097840 T 
			"_Z32register_android_hardware_CameraP7_JNIEnv",
// 00096770 T 
			"_Z32register_android_media_JetPlayerP7_JNIEnv",
// 00094e5c T 
			"_Z33register_android_media_AudioTrackP7_JNIEnv",
// 0005ec54 T 
			"_Z33register_android_opengl_jni_EGL14P7_JNIEnv",
// 00089b10 T 
			"_Z34register_android_graphics_GraphicsP7_JNIEnv",
// 00088ad8 T 
			"_ZN7android36register_android_graphics_FontFamilyEP7_JNIEnv",
// 00090f0c T 
			"_Z34register_android_graphics_TypefaceP7_JNIEnv",
// 00091a4c T 
			"_Z34register_android_graphics_YuvImageP7_JNIEnv",
// 000925e0 T 
			"_Z34register_android_media_AudioRecordP7_JNIEnv",
// 00094580 T 
			"_Z34register_android_media_AudioSystemP7_JNIEnv",
// 0005f244 T 
			"_Z34register_android_opengl_jni_EGLExtP7_JNIEnv",
// 00060cec T 
			"_Z34register_android_opengl_jni_GLES10P7_JNIEnv",
// 00062ee4 T 
			"_Z34register_android_opengl_jni_GLES11P7_JNIEnv",
// 000692cc T 
			"_Z34register_android_opengl_jni_GLES20P7_JNIEnv",
// 0006c8dc T 
			"_Z34register_android_opengl_jni_GLES30P7_JNIEnv",
// 0006e7f4 T 
			"_Z34register_android_opengl_jni_GLES31P7_JNIEnv",
// 0008b46c T 
			"_Z35register_android_graphics_NinePatchP7_JNIEnv",
// 000a1318 T 
			"_Z35register_android_hardware_UsbDeviceP7_JNIEnv",
// 0008a4b0 T 
			"_Z36register_android_graphics_MaskFilterP7_JNIEnv",
// 0008e57c T 
			"_Z36register_android_graphics_PathEffectP7_JNIEnv",
// 0009fcd8 T 
			"_Z36register_android_hardware_SerialPortP7_JNIEnv",
// 000a1ad8 T 
			"_Z36register_android_hardware_UsbRequestP7_JNIEnv",
// 00097014 T 
			"_Z36register_android_media_ToneGeneratorP7_JNIEnv",
// 000610a4 T 
			"_Z37register_android_opengl_jni_GLES10ExtP7_JNIEnv",
// 00064f58 T 
			"_Z37register_android_opengl_jni_GLES11ExtP7_JNIEnv",
// 0006f3b4 T 
			"_Z37register_android_opengl_jni_GLES31ExtP7_JNIEnv",
// 0008a338 T 
			"_Z38register_android_graphics_InterpolatorP7_JNIEnv",
// 000a057c T 
			"_Z38register_android_hardware_SoundTriggerP7_JNIEnv",
// 0009f1bc T 
			"_Z39register_android_hardware_SensorManagerP7_JNIEnv",
// 00057b40 T 
			"_Z43register_com_google_android_gles_jni_GLImplP7_JNIEnv",
// 0009b4f8 T 
			"_Z44register_android_hardware_camera2_DngCreatorP7_JNIEnv",
// 0005d074 T 
			"_Z44register_com_google_android_gles_jni_EGLImplP7_JNIEnv",
// 0008eb10 T 
			"_Z45register_android_graphics_BitmapRegionDecoderP7_JNIEnv",
// 000a176c T 
			"_Z45register_android_hardware_UsbDeviceConnectionP7_JNIEnv",
// 00099228 T 
			"_Z48register_android_hardware_camera2_CameraMetadataP7_JNIEnv",
// 00088ea0 T 
			"_Z55register_android_graphics_CreateJavaOutputStreamAdaptorP7_JNIEnv",
// 0009a80c T 
			"_Z56register_android_hardware_camera2_legacy_PerfMeasurementP7_JNIEnv",
// 00099624 T 
			"_Z59register_android_hardware_camera2_legacy_LegacyCameraDeviceP7_JNIEnv",
// 000a201c T 
			"_Z62register_android_hardware_location_ActivityRecognitionHardwareP7_JNIEnv",
// 0005a9c8 T 
			// "_ZN7android14AndroidRuntime21registerNativeMethodsEP7_JNIEnv",
// 0007db04 T 
			"_ZN7android36register_android_os_SystemPropertiesEP7_JNIEnv",
// 0007c0a0 T 
			"_ZN7android25register_android_os_DebugEP7_JNIEnv",
// 0007df80 T 
			"_ZN7android25register_android_os_TraceEP7_JNIEnv",
// 0007f4cc T 
			"_ZN7android26register_android_nio_utilsEP7_JNIEnv",
// 0007d08c T 
			// "_ZN7android26register_android_os_ParcelEP7_JNIEnv",
// 0007d41c T 
			"_ZN7android27register_android_os_SELinuxEP7_JNIEnv",
// 0007c4a4 T 
			"_ZN7android30register_android_os_MemoryFileEP7_JNIEnv",
// 0008319c T 
			"_ZN7android30register_android_util_EventLogEP7_JNIEnv",
// 00075c24 T 
			"_ZN7android30register_android_view_KeyEventEP7_JNIEnv",
// 0008cd20 T 
			"_ZN7android31register_android_graphics_PaintEP7_JNIEnv",
// 0005a454 T 
			"_ZN7android31register_android_opengl_classesEP7_JNIEnv",
// 0007d71c T 
			"_ZN7android31register_android_os_SystemClockEP7_JNIEnv",
// 0008a8b4 T 
			"_ZN7android32register_android_graphics_MatrixEP7_JNIEnv",
// 0008f47c T 
			"_ZN7android32register_android_graphics_RegionEP7_JNIEnv",
// 000a2160 T 
			"_ZN7android32register_android_os_FileObserverEP7_JNIEnv",
// 0007c634 T 
			"_ZN7android32register_android_os_MessageQueueEP7_JNIEnv",
// 000a27d8 T 
			"_ZN7android32register_android_server_WatchdogEP7_JNIEnv",
// 000752cc T 
			"_ZN7android32register_android_view_InputQueueEP7_JNIEnv",
// 0007a148 T 
			"_ZN7android32register_android_view_RenderNodeEP7_JNIEnv",
// 00084ed8 T 
			"_ZN7android33register_android_content_XmlBlockEP7_JNIEnv",
// 000860c8 T 
			"_ZN7android33register_android_graphics_PictureEP7_JNIEnv",
// 0007efc4 T 
			"_ZN7android33register_android_net_NetworkUtilsEP7_JNIEnv",
// 0007f410 T 
			"_ZN7android33register_android_net_TrafficStatsEP7_JNIEnv",
// 0007abfc T 
			"_ZN7android33register_android_text_AndroidBidiEP7_JNIEnv",
// 00073960 T 
			"_ZN7android33register_android_view_InputDeviceEP7_JNIEnv",
// 0007929c T 
			"_ZN7android33register_android_view_MotionEventEP7_JNIEnv",
// 00079628 T 
			"_ZN7android33register_android_view_PointerIconEP7_JNIEnv",
// 00073060 T 
			"_ZN7android33register_android_view_TextureViewEP7_JNIEnv",
// 00091488 T 
			"_ZN7android34register_android_graphics_XfermodeEP7_JNIEnv",
// 0007e224 T 
			"_ZN7android34register_android_os_UEventObserverEP7_JNIEnv",
// 0007ac14 T 
			"_ZN7android34register_android_text_StaticLayoutEP7_JNIEnv",
// 00077344 T 
			"_ZN7android34register_android_view_GLES20CanvasEP7_JNIEnv",
// 0007354c T 
			"_ZN7android34register_android_view_InputChannelEP7_JNIEnv",
// 00077398 T 
			"_ZN7android35register_android_app_ActivityThreadEP7_JNIEnv",
// 0005d618 T 
			"_ZN7android35register_android_app_NativeActivityEP7_JNIEnv",
// 00070f60 T 
			"_ZN7android35register_android_emoji_EmojiFactoryEP7_JNIEnv",
// 000762fc T 
			"_ZN7android35register_android_view_GraphicBufferEP7_JNIEnv",
// 00077bc0 T 
			"_ZN7android35register_android_view_HardwareLayerEP7_JNIEnv",
// 000a57c0 T 
			"_ZN7android35register_org_codeaurora_PerformanceEP7_JNIEnv",
// 00084af0 T 
			"_ZN7android36register_android_content_StringBlockEP7_JNIEnv",
// 00088840 T 
			"_ZN7android36register_android_graphics_DrawFilterEP7_JNIEnv",
// 0008e87c T 
			"_ZN7android36register_android_graphics_PorterDuffEP7_JNIEnv",
// 0008efb4 T 
			"_ZN7android36register_android_graphics_RasterizerEP7_JNIEnv",
// 00096bb8 T 
			"_ZN7android36register_android_media_RemoteDisplayEP7_JNIEnv",
// 0007eb34 T 
			"_ZN7android36register_android_net_LocalSocketImplEP7_JNIEnv",
// 000722e4 T 
			"_ZN7android36register_android_view_SurfaceControlEP7_JNIEnv",
// 00072ff0 T 
			"_ZN7android36register_android_view_SurfaceSessionEP7_JNIEnv",
// 000814a0 T 
			"_ZN7android37register_android_content_AssetManagerEP7_JNIEnv",
// 00070ba8 T 
			"_ZN7android37register_android_database_SQLiteDebugEP7_JNIEnv",
// 000886fc T 
			"_ZN7android37register_android_graphics_ColorFilterEP7_JNIEnv",
// 0008e434 T 
			"_ZN7android37register_android_graphics_PathMeasureEP7_JNIEnv",
// 00075fc8 T 
			"_ZN7android37register_android_view_KeyCharacterMapEP7_JNIEnv",
// 0007a748 T 
			"_ZN7android37register_android_view_VelocityTrackerEP7_JNIEnv",
// 000a35ec T 
			"_ZN7android38register_android_app_backup_FullBackupEP7_JNIEnv",
// 0006fb9c T 
			"_ZN7android38register_android_database_CursorWindowEP7_JNIEnv",
// 00070af0 T 
			"_ZN7android38register_android_database_SQLiteGlobalEP7_JNIEnv",
// 0007ab14 T 
			"_ZN7android38register_android_text_AndroidCharacterEP7_JNIEnv",
// 000748cc T 
			"_ZN7android38register_android_view_InputEventSenderEP7_JNIEnv",
// 00077ec8 T 
			"_ZN7android38register_android_view_ThreadedRendererEP7_JNIEnv",
// 000a2d50 T 
			"_ZN7android39register_android_backup_BackupDataInputEP7_JNIEnv",
// 000a3640 T 
			"_ZN7android39register_android_content_res_ObbScannerEP7_JNIEnv",
// 00091eac T 
			"_ZN7android39register_android_graphics_pdf_PdfEditorEP7_JNIEnv",
// 000a5300 T 
			"_ZN7android39register_com_android_internal_os_ZygoteEP7_JNIEnv",
// 000a2f10 T 
			"_ZN7android40register_android_backup_BackupDataOutputEP7_JNIEnv",
// 000a2950 T 
			"_ZN7android40register_android_ddm_DdmHandleNativeHeapEP7_JNIEnv",
// 00088664 T 
			"_ZN7android40register_android_graphics_CanvasPropertyEP7_JNIEnv",
// 000909dc T 
			"_ZN7android40register_android_graphics_SurfaceTextureEP7_JNIEnv",
// 00073f78 T 
			"_ZN7android40register_android_view_InputEventReceiverEP7_JNIEnv",
// 0007a434 T 
			"_ZN7android40register_android_view_RenderNodeAnimatorEP7_JNIEnv",
// 0008efcc T 
			"_ZN7android41register_android_graphics_LayerRasterizerEP7_JNIEnv",
// 00091cf4 T 
			"_ZN7android41register_android_graphics_pdf_PdfDocumentEP7_JNIEnv",
// 00092298 T 
			"_ZN7android41register_android_graphics_pdf_PdfRendererEP7_JNIEnv",
// 000a38e4 T 
			"_ZN7android42register_android_content_res_ConfigurationEP7_JNIEnv",
// 000709ec T 
			"_ZN7android42register_android_database_SQLiteConnectionEP7_JNIEnv",
// 000712c4 T 
			"_ZN7android42register_android_view_DisplayEventReceiverEP7_JNIEnv",
// 000a2bb4 T 
			"_ZN7android43register_com_android_internal_os_ZygoteInitEP7_JNIEnv",
// 000a30b4 T 
			"_ZN7android44register_android_backup_FileBackupHelperBaseEP7_JNIEnv",
// 0005a740 T 
			"_ZN7android44register_com_android_internal_os_RuntimeInitEP7_JNIEnv",
// 000a33e0 T 
			"_ZN7android46register_android_backup_BackupHelperDispatcherEP7_JNIEnv",
// 000a3d00 T 
			"_ZN7android47register_android_animation_PropertyValuesHolderEP7_JNIEnv",
// 000a5398 T 
			"_ZN7android52register_com_android_internal_util_VirtualRefBasePtrEP7_JNIEnv",
// 000a25c4 T 
			"_ZN7android53register_android_server_NetworkManagementSocketTaggerEP7_JNIEnv",
// 000a4528 T 
			"_ZN7android53register_com_android_internal_net_NetworkStatsFactoryEP7_JNIEnv",
// 000a248c T 
			"_ZN7android54register_android_server_fingerprint_FingerprintServiceEP7_JNIEnv",
// 0005c2c4 T 
			"_ZN7android57register_com_android_internal_content_NativeLibraryHelperEP7_JNIEnv",
// 000a55c0 T 
			"_ZN7android76register_com_android_internal_view_animation_NativeInterpolatorFactoryHelperEP7_JNIEnv",
			

// 00087464 T 
			"_Z39register_android_graphics_BitmapFactoryP7_JNIEnv"
*/
		};
	}
	
	private static native int nativeRegisterNatives(String registerSymbol);
	public static void tryRegisterNatives(String className) {

		// Find class or ignore if ClassNotFoundException
		int indexRegister = className.indexOf("register_");
		int indexP7Env = className.indexOf("P7_JNIEnv");
		boolean isInNamespace = (className.startsWith("_ZN3art") || className.startsWith("_ZN7android")) && className.endsWith("EP7_JNIEnv");
		String classStr = className.substring(indexRegister + 9, indexP7Env - (isInNamespace ? 1 : 0)).replace('_', '.');
		try {
			Class.forName(classStr);
		} catch (ClassNotFoundException unused) {
			return;
		} catch (UnsatisfiedLinkError e) {
			// A special case that need add registerNatives code in static block
			throw new RuntimeException(
				e.getStackTrace()[0].getClassName() + " seems a special case that need add registerNatives code in static block",
				e
			);
		}

		int result = nativeRegisterNatives(className);
		// Debug
		System.out.println("Register native for " + classStr + " returned " + Integer.toString(result));
	}
	
	public static void registerNatives() {
		System.out.println("Register native classes: " + Integer.toString(nativeClassArr.length));
		
		// This may cause huge lag because of ClassNotFoundException
		for (String nativeClass : nativeClassArr) {
			tryRegisterNatives(nativeClass);
		}
	}
}
