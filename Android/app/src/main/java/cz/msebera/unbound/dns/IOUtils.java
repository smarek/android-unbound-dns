/*
    Copyright (c) 2015 Marek Sebera <marek.sebera@gmail.com>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package cz.msebera.unbound.dns;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class IOUtils {

    public static void createConfigFromDefault(File directory, Context ctx) {
        try {
            File confFile = new File(directory, ctx.getString(R.string.path_unbound_conf));
            if (!confFile.exists()) {
                File defaultConfFile = new File(directory, ctx.getString(R.string.path_unbound_conf_default));
                if (defaultConfFile.exists()) {
                    FileChannel in = new FileInputStream(defaultConfFile).getChannel();
                    FileChannel out = new FileOutputStream(confFile).getChannel();
                    out.transferFrom(in, 0, in.size());
                }
            }
        } catch (Throwable t) {
            Log.e("createConfigFromDef()", "Cannot copy default configuration", t);
        }
    }

    public static void unzip(File zipFile, File targetDirectory) {
        try {
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(new FileInputStream(zipFile)));
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
            zis.close();
        } catch (Throwable t) {
            Log.e("unzip()", "Error while copying", t);
        }
    }

    public static boolean copyFile(Context ctx, String filename) {
        AssetManager assetManager = ctx.getAssets();

        InputStream in;
        OutputStream out;
        String newFileName = null;
        try {
            Log.i("copyFile()", "" + filename);
            in = assetManager.open(filename);
            newFileName = ctx.getFilesDir().getAbsolutePath() + File.separator + filename;
            File outFile = new File(newFileName);
            if (outFile.exists()) {
                Log.d("copyFile()", "copy delete success? " + outFile.delete());
            }
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            return true;
        } catch (Throwable e) {
            Log.e("copyFile()", "Exception in copyFile() of " + newFileName);
            Log.e("copyFile()", "Exception in copyFile() " + e.toString());
        }
        return false;
    }

}
