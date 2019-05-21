package nikhiljain.com.keystoreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SAMPLE_ALIAS = "MYALIAS";

//    @BindView (R.id.ed_text_to_encrypt)
    EditText edTextToEncrypt;

//    @BindView (R.id.tv_encrypted_text)
    TextView tvEncryptedText;

//    @BindView(R.id.tv_decrypted_text)
    TextView tvDecryptedText;

    private Encryptor encryptor;
    private DeCryptor decryptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);

        prepareViews();

        encryptor = new Encryptor();

        try {
            decryptor = new DeCryptor();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException |
                IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareViews() {
        edTextToEncrypt = findViewById(R.id.ed_text_to_encrypt);
        tvEncryptedText = findViewById(R.id.tv_encrypted_text);
        tvDecryptedText = findViewById(R.id.tv_decrypted_text);
    }

    //    @OnClick({R.id.btn_encrypt, R.id.btn_decrypt})
    public void onClick(final View view) {

        final int id = view.getId();

        switch (id) {
            case R.id.btn_encrypt:
                encryptText();
                break;
            case R.id.btn_decrypt:
                decryptText();
                break;
        }
    }

    private void decryptText() {
        try {
            tvDecryptedText.setText(decryptor
                    .decryptData(SAMPLE_ALIAS, encryptor.getEncryption(), encryptor.getIv()));
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                KeyStoreException | NoSuchPaddingException | NoSuchProviderException |
                IOException | InvalidKeyException e) {
            Log.e(TAG, "decryptData() called with: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    private void encryptText() {

        try {
            final byte[] encryptedText = encryptor
                    .encryptText(SAMPLE_ALIAS, edTextToEncrypt.getText().toString());
            tvEncryptedText.setText(Base64.encodeToString(encryptedText, Base64.DEFAULT));
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchProviderException |
                KeyStoreException | IOException | NoSuchPaddingException | InvalidKeyException e) {
            Log.e(TAG, "onClick() called with: " + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException | SignatureException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }
}