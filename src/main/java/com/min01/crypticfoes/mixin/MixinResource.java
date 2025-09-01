package com.min01.crypticfoes.mixin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;

@Mixin(Resource.class)
public class MixinResource 
{
    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH = 128;

    private static final String MASTER_KEY = "2hg0834h8cidnkfj!!gmsgk";
    private static final String SALT = "Ei499Rj39f!-f-k";
    private static final String HEADER = "CRYPTICF";
    
    private static final byte[] MAGIC_HEADER = HEADER.getBytes(StandardCharsets.UTF_8);

	@Shadow
	@Final
	private IoSupplier<InputStream> streamSupplier;
	 
    @Inject(method = "open", at = @At("HEAD"), cancellable = true)
    private void open(CallbackInfoReturnable<InputStream> cir) throws IOException 
    {
        byte[] data = this.streamSupplier.get().readAllBytes();
    	cir.setReturnValue(decrypt(data));
    }
    
    private static SecretKey getMasterKey() throws NoSuchAlgorithmException, InvalidKeySpecException 
    {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(MASTER_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
    
    private static ByteArrayInputStream decrypt(byte[] data)
    {
        if(data.length > 8 && new String(data, 0, 8, StandardCharsets.UTF_8).equals(HEADER)) 
        {
			try 
			{
				ByteArrayInputStream byteArray = decryptFile(data);
	        	return byteArray;
			}
			catch(Exception e) 
			{
				e.printStackTrace();
			}
        }
    	return new ByteArrayInputStream(data);
    }
    
    private static ByteArrayInputStream decryptFile(byte[] encryptedData) throws Exception 
    {
        SecretKey key = getMasterKey();
        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);
        byte[] magic = new byte[MAGIC_HEADER.length];
        byteBuffer.get(magic);
        if(!java.util.Arrays.equals(magic, MAGIC_HEADER)) 
        {
            return null;
        }
        byte[] iv = new byte[IV_SIZE];
        byteBuffer.get(iv);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] cipherBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherBytes);
        byte[] plainBytes = cipher.doFinal(cipherBytes);
        return new ByteArrayInputStream(plainBytes);
    }
}