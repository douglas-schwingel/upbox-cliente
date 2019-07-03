package br.com.upbox.codec;

import br.com.upbox.models.Usuario;
import com.mongodb.BasicDBObject;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UsuarioCodec implements CollectibleCodec<Usuario> {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioCodec.class);
    private static final Marker marker = MarkerFactory.getMarker("usuario-codec");

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
        if(!documentHasId(usuario)) logger.error(marker,"Usuário {} não tem ID", usuario.getUsername());
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
        usuario.setUsername(document.getString("username"));
        usuario.setSenha(document.getString("senha"));
        ArrayList<Document> lista = (ArrayList<Document>) document.get("compartilhadosComigo");
        Set<BasicDBObject> set = new HashSet<>();
        for (Document item : lista) {
            BasicDBObject object = new BasicDBObject();
            object.put("owner", item.getString("owner"));
            object.put("arquivo", item.getString("arquivo"));
            set.add(object);
        }
        usuario.setArquivosCompartilhados(set);
        return usuario;
    }

    private Document criaDocument(Usuario usuario) {
        Document document = new Document();
        document.put("_id", usuario.getId());
        document.put("uuid", usuario.getUuid().toString());
        document.put("nome", usuario.getNome());
        document.put("email", usuario.getEmail());
        document.put("username", usuario.getUsername());
        document.put("compartilhadosComigo", usuario.getArquivosCompartilhados());
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
