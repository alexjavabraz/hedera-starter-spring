package hedera.starter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Creates a ERC-20 Token with contract Params and Network fee and gas")
public class TokenDTO {


    @ApiModelProperty(name = "bytecode", example = "608060405260006001601461010...", required = true)
    public String byteCode;

    @ApiModelProperty(name = "tokenName", example = "XYZ Token", required = true)
    public String tokenName;

    @ApiModelProperty(name = "tokenSymbol",  example = "The XYZ Coin for XYZ Company", required = true)
    public String tokenSymbol;

    @ApiModelProperty(name = "initialSupply", example = "1000000000", required = true)
    public Long initialSupply;

    @ApiModelProperty(name = "hBarMaxTransactionFee", example = "20", required = false)
    public Integer hBarMaxTransactionFee = 20;

    @ApiModelProperty(name = "gasToDeployContractValue", example = "400000", required = false)
    public Long gasToDeployContractValue = 400000L;
}
