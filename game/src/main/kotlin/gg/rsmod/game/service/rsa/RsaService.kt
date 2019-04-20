package gg.rsmod.game.service.rsa

import gg.rsmod.game.Server
import gg.rsmod.game.model.World
import gg.rsmod.game.service.Service
import gg.rsmod.util.ServerProperties
import mu.KLogging
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
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class RsaService : Service {

    private lateinit var keyPath: Path

    private lateinit var exponent: BigInteger

    private lateinit var modulus: BigInteger

    private var radix = -1

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        keyPath = Paths.get(serviceProperties.getOrDefault("path", "./data/rsa/key.pem"))
        radix = serviceProperties.getOrDefault("radix", 16)

        if (!Files.exists(keyPath)) {
            val scanner = Scanner(System.`in`)
            println("Private RSA key was not found in path: $keyPath")
            println("Would you like to create one? (y/n)")

            val create = if (scanner.hasNext()) scanner.nextLine() in arrayOf("yes", "y", "true") else true
            if (create) {
                logger.info("Generating RSA key pair...")
                createPair(bitCount = serviceProperties.getOrDefault("bit-count", 2048))
                println("Please follow the instructions on console and continue once you've done so.")
                scanner.next()
                init(server, world, serviceProperties)
            } else {
                throw RuntimeException("Private RSA key was not found! Please follow the instructions on console.")
            }
        }

        try {
            PemReader(Files.newBufferedReader(keyPath)).use { reader ->
                val pem = reader.readPemObject()
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

    override fun bindNet(server: Server, world: World) {
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
        println("Place these keys in the client (find BigInteger(\"10001\" in client code):")
        println("--------------------")
        println("public key: " + publicKey.publicExponent.toString(radix))
        println("modulus: " + publicKey.modulus.toString(radix))
        println("")

        try {
            PemWriter(Files.newBufferedWriter(keyPath)).use { writer ->
                writer.writeObject(PemObject("RSA PRIVATE KEY", privateKey.encoded))
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to write private key to ${keyPath.toAbsolutePath()}" }
        }
    }

    fun getExponent(): BigInteger = exponent

    fun getModulus(): BigInteger = modulus

    companion object : KLogging() {

        @JvmStatic
        fun main(args: Array<String>) {
            val radix = args[0].toInt()
            val bitCount = args[1].toInt()
            val path = args[2]

            val service = RsaService()
            service.keyPath = Paths.get(path)
            service.radix = radix

            val directory = service.keyPath.parent.toAbsolutePath()
            if (!Files.exists(directory)) {
                Files.createDirectory(directory)
            }

            logger.info("Generating RSA key pair...")
            service.createPair(bitCount)
        }
    }
}
