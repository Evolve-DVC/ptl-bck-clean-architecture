package com.empresa.plantilla.commons.util.security;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

/**
 * A utility class for encoding and decoding strings using Base64 encoding.
 */
@Slf4j
@Getter
@Setter
public class Cripto {
    private Base64 base64;
    private String encodedVersion;
    private String decodedVersion;

    /**
     * Constructs a new Cripto object and initializes the Base64 encoder/decoder.
     */
    public Cripto() {
        setBase64(new Base64());
    }

    /**
     * Encodes a given string to Base64 format.
     *
     * @param cadena The string to be encoded.
     * @return The Base64 encoded string.
     * @throws Exception If an encoding error occurs.
     */
    public String encodeBase64(String cadena) throws Exception {
        setEncodedVersion(new String(base64.encode(cadena.getBytes())));
        return encodedVersion;
    }

    /**
     * Decodes a Base64 encoded string.
     *
     * @param cadena The Base64 encoded string to be decoded.
     * @return The decoded string.
     * @throws Exception If a decoding error occurs.
     */
    public String decodeBAse64(String cadena) throws Exception {
        setDecodedVersion(new String(base64.decode(cadena.getBytes())));
        return decodedVersion;
    }
}
