package br.com.upbox.codec;

import br.com.upbox.models.Usuario;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioCodec implements CollectibleCodec<Usuario> {
    private static final Logger logger = Logger.getLogger(UsuarioCodec.class.getName());

    private Codec<Document> codec;

    public UsuarioCodec(Codec<Document> codec) {
        this.codec = codec;
    }


    @Override
    public Usuario generateIdIfAbsentFromDocument(Usuario usuario) {
        return documentHasId(usuario) ? usuario.criarId() : usuario;
    }

    @Override
    public boolean documentHasId(Usuario usuario) {
        return usuario.getId() == null;
    }

    @Override
    public BsonValue getDocumentId(Usuario usuario) {
        if(!documentHasId(usuario)) logger.log(Level.WARNING,"Usuário não tem ID");
        return new BsonString(usuario.getId().toHexString());
    }

    @Override
    public Usuario decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = codec.decode(reader, decoderContext);
        return populaUsuario(document);
    }

    @Override
    public void encode(BsonWriter writer, Usuario usuario, EncoderContext encoderContext) {
        codec.encode(writer, criaDocument(usuario), encoderContext);
    }

    //    Métodos auxiliares - Não fazem parte da interface CollectibleCodec

    private Usuario populaUsuario(Document document) {
        Usuario usuario = new Usuario();
        usuario.setId(document.getObjectId("_id"));
        usuario.setUuid(UUID.fromString(document.getString("uuid")));
        usuario.setNome(document.getString("nome"));
        usuario.setEmail(document.getString("email"));
        Instant dataInstant = document.getDate("dataNascimento").toInstant();
//        usuario.setDataNascimento(LocalDate.ofInstant(dataInstant, ZoneId.systemDefault()));
        usuario.setUsername(document.getString("username"));
        usuario.setSenha(document.getString("senha"));
        return usuario;
    }

    public Document criaDocument(Usuario usuario) {
        Document document = new Document();
        document.put("_id", usuario.getId());
        document.put("uuid", usuario.getUuid().toString());
        document.put("nome", usuario.getNome());
        document.put("email", usuario.getEmail());
        document.put("username", usuario.getUsername());
//        document.put("dataNascimento", usuario.getDataNascimento());
        String salto = BCrypt.gensalt();
        String senhaHash = BCrypt.hashpw(usuario.getSenha(), salto);
        document.put("senha", senhaHash);
        return document;
    }

    @Override
    public Class<Usuario> getEncoderClass() {
        return Usuario.class;
    }
}
