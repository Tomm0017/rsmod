package gg.rsmod.game.service.rsa

import gg.rsmod.game.Server
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import org.apache.logging.log4j.LogManager
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemReader
import org.bouncycastle.util.io.pem.PemWriter
import java.io.IOException
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.Security
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec

/**
 * @author Tom <rspsmods@gmail.com>
 */
class RsaService : Service() {

    companion object {
        private val logger = LogManager.getLogger(RsaService::class.java)
    }

    private lateinit var keyPath: Path

    private lateinit var exponent: BigInteger

    private lateinit var modulus: BigInteger

    @Throws(RuntimeException::class)
    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        keyPath = Paths.get(serviceProperties.getOrDefault("path", "./data/rsa/key.pem"))

        if (!Files.exists(keyPath)) {
            logger.info("Generating RSA key pair...")
            createPair(bitCount = serviceProperties.getOrDefault("bit-count", 2048))
            throw RuntimeException("Private RSA key was not found! Please follow the instructions on console.")
        }

        try {
            PemReader(Files.newBufferedReader(keyPath)).use { pemReader ->
                val pem = pemReader.readPemObject()
                val keySpec = PKCS8EncodedKeySpec(pem.content)

                Security.addProvider(BouncyCastleProvider())
                val factory = KeyFactory.getInstance("RSA", "BC")

                val privateKey = factory.generatePrivate(keySpec) as RSAPrivateKey
                exponent = privateKey.privateExponent
                modulus = privateKey.modulus
            }
        } catch (exception: Exception) {
            throw ExceptionInInitializerError(IOException("Error parsing RSA key pair: ${keyPath.toAbsolutePath()}", exception))
        }
    }

    override fun postLoad(server: Server, world: World) {
    }

    override fun terminate(server: Server, world: World) {
    }

    /**
     * Credits: Apollo
     *
     * @author Graham
     * @author Major
     * @author Cube
     */
    private fun createPair(bitCount: Int) {
        Security.addProvider(BouncyCastleProvider())

        val keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC")
        keyPairGenerator.initialize(bitCount)
        val keyPair = keyPairGenerator.generateKeyPair()

        val privateKey = keyPair.private as RSAPrivateKey
        val publicKey = keyPair.public as RSAPublicKey

        println("")
        println("Place these keys in the client and restart the server & client:")
        println("--------------------")
        println("public key: " + publicKey.publicExponent)
        println("modulus: " + publicKey.modulus)
        println("")

        try {
            PemWriter(Files.newBufferedWriter(keyPath)).use { writer ->
                writer.writeObject(PemObject("RSA PRIVATE KEY", privateKey.encoded))
            }
        } catch (e: Exception) {
            System.err.println("Failed to write private key to ${keyPath.toAbsolutePath()}")
            e.printStackTrace()
        }

    }

    fun getExponent(): BigInteger = exponent

    fun getModulus(): BigInteger = modulus
}