package a.b.c;

import java.util.Collections;
import java.util.List;

import a.b.c.impl.CrossPlatformResource;
import a.b.c.impl.CrossPlatformService;
import a.b.c.impl.InvalidOperationException;
import org.apache.thrift.TException;

public class CrossPlatformServiceImpl implements CrossPlatformService.Iface {
  @Override
  public CrossPlatformResource get(final int id)
      throws InvalidOperationException, TException {
    return new CrossPlatformResource();
  }

  @Override
  public void save(final CrossPlatformResource resource)
      throws InvalidOperationException, TException {

  }

  @Override
  public List<CrossPlatformResource> getList()
      throws InvalidOperationException, TException {
    return Collections.emptyList();
  }

  @Override
  public boolean ping() throws InvalidOperationException, TException {
    return true;
  }
}
