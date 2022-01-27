package hedera.starter.utilities;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.file.FileAppendTransaction;
import com.hedera.hashgraph.sdk.file.FileCreateTransaction;
import com.hedera.hashgraph.sdk.file.FileId;

import java.time.Duration;
import java.time.Instant;

public class Utils {
    public static final int FILE_PART_SIZE = 5000;

    public static byte[] copyBytes(int start, int length, byte[] bytes) {
        byte[] rv = new byte[length];
        for (int i = 0; i < length; i++) {
            rv[i] = bytes[start + i];
        }
        return rv;
    }

    //HELPER- create a bytecode file.
    public static FileId createBytecodeFile(String byteCodeHex, Client client) throws HederaStatusException {
        byte[] byteCode = byteCodeHex.getBytes();

        int numParts = byteCode.length / FILE_PART_SIZE;
        int remainder = byteCode.length % FILE_PART_SIZE;
        // add in 5k chunks
        byte[] firstPartBytes;
        if (byteCode.length <= FILE_PART_SIZE) {
            firstPartBytes = byteCode;
            remainder = 0;
        } else {
            firstPartBytes = Utils.copyBytes(0, FILE_PART_SIZE, byteCode);
        }

        // create the contract's bytecode file
        var fileTxId =
                new FileCreateTransaction()
                        .setExpirationTime(Instant.now().plus(Duration.ofMillis(7890000000L)))
                        // Use the same key as the operator to "own" this file
                        .addKey(EnvUtils.getOperatorKey().publicKey)
                        .setContents(firstPartBytes)
                        .setMaxTransactionFee(new Hbar(5))
                        .execute(client);

        var fileReceipt = fileTxId.getReceipt(client);
        FileId newFileId = fileReceipt.getFileId();

        System.out.println("Bytecode file ID: " + newFileId);

        // add remaining chunks
        // append the rest of the parts
        for (int i = 1; i < numParts; i++) {
            byte[] partBytes = Utils.copyBytes(i * FILE_PART_SIZE, FILE_PART_SIZE, byteCode);
            new FileAppendTransaction()
                    .setFileId(newFileId)
                    .setMaxTransactionFee(new Hbar(5))
                    .setContents(partBytes)
                    .execute(client);
        }
        // appending remaining data
        if (remainder > 0) {
            byte[] partBytes = Utils.copyBytes(numParts * FILE_PART_SIZE, remainder, byteCode);
            new FileAppendTransaction()
                    .setFileId(newFileId)
                    .setMaxTransactionFee(new Hbar(5))
                    .setContents(partBytes)
                    .execute(client);
        }

        return newFileId;
    }

}
