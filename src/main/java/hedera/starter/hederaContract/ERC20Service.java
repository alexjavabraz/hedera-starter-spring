package hedera.starter.hederaContract;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.HederaStatusException;
import com.hedera.hashgraph.sdk.contract.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.contract.ContractFunctionParams;
import com.hedera.hashgraph.sdk.contract.ContractId;
import com.hedera.hashgraph.sdk.file.FileId;
import hedera.starter.dto.TokenDTO;
import hedera.starter.utilities.HederaClient;
import org.springframework.stereotype.Service;
import static hedera.starter.utilities.Utils.createBytecodeFile;

import java.math.BigInteger;
import java.time.Duration;

@Service
public class ERC20Service {
    public Client client = HederaClient.getHederaClientInstance();

    public String create(TokenDTO token) throws HederaStatusException {
        FileId bytecodeFile = createBytecodeFile(token.byteCode, client);
        var contractTxId =
                new ContractCreateTransaction()
                        .setBytecodeFileId(bytecodeFile)
                        .setAutoRenewPeriod(Duration.ofSeconds(8000000))
                        .setMaxTransactionFee(new Hbar(token.hBarMaxTransactionFee))
                        .setGas(token.gasToDeployContractValue)
                        .setConstructorParams(
                                new ContractFunctionParams()
                                        .addString(token.tokenName)
                                        .addString(token.tokenSymbol)
                                        .addInt256(BigInteger.valueOf(token.initialSupply))
                        )
                        .execute(client);

        var contractReceipt = contractTxId.getReceipt(client);
        ContractId newContractId = contractReceipt.getContractId();

        return newContractId.toString();
    }
}
