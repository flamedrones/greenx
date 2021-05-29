package com.flamecode.greenx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class FGXManager {

    @Value("${fgx.config.privateKey}")
    String privateKey;
    @Value("${fgx.config.contractAddress}")
    String contractAddress;

    private static final Logger LOGGER = LoggerFactory.getLogger(FGXManager.class);

    public static String encodeTransferData(String toAddress, BigInteger sum) {
        Function function = new Function(
                "transfer",  // function we're calling
                Arrays.asList(new Address(toAddress), new Uint256(sum)),  // Parameters to pass as Solidity Types
                Arrays.asList(new org.web3j.abi.TypeReference<Bool>() {
                }));
        return FunctionEncoder.encode(function);
    }

    public String sendFGX(Integer amount, String toAddress) throws Exception {
        Web3j web3 = Web3j.build(new HttpService("https://ropsten.infura.io/v3/c2de2798029b4fb584ecd43e74b73ebc"));
        Credentials creds = Credentials.create(this.privateKey);
        RawTransactionManager manager = new RawTransactionManager(web3, creds);
        String contractAddress = this.contractAddress;
        BigInteger sum = BigInteger.valueOf(amount).multiply(BigInteger.valueOf(1000000000)).multiply(BigInteger.valueOf(10));
        String data = encodeTransferData(toAddress, sum);
        BigInteger gasPrice = web3.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(120000); // set gas limit here
        EthSendTransaction transaction = manager.sendTransaction(gasPrice, gasLimit, contractAddress, data, null);
        return transaction.getTransactionHash();
    }

    public static Map<String, String> generateWallet() {
        var processJson = new HashMap<String, String>();
        var seed = UUID.randomUUID().toString();
        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();
            String sPrivatekeyInHex = privateKeyInDec.toString(16);
            WalletFile aWallet = Wallet.createLight(seed, ecKeyPair);
            String sAddress = aWallet.getAddress();
            processJson.put("address", "0x" + sAddress);
            processJson.put("privateKey", sPrivatekeyInHex);
        } catch (CipherException | NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            LOGGER.error("", e);
        }
        return processJson;
    }

}
