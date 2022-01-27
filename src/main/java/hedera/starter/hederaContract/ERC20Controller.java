package hedera.starter.hederaContract;


import com.hedera.hashgraph.sdk.HederaStatusException;
import hedera.starter.dto.TokenDTO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("Handles management of ERC-20 Hedera Contract related Services")
@RequestMapping(path = "/token")
public class ERC20Controller {

    @Autowired
    ERC20Service contractService;

    @PostMapping("")
    @ApiOperation("Create a Contract with a bytecode. NOTE- Can only contracts with 3 params in constructor, to deploy contract without param, use /contract.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Contract ID")})
    public String createContractWithGas(
            @RequestBody TokenDTO tokenDTO) throws HederaStatusException {
        return contractService.create(tokenDTO);
    }
}
