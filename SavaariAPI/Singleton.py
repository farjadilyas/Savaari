class Singleton(object):
    instances = {}
    def __new__(cls, clz = None):
        if clz is None:
            # print ("Creating object for", cls)
            if not cls.__name__ in Singleton.instances:
                Singleton.instances[cls.__name__] = \
                    object.__new__(cls)
            return Singleton.instances[cls.__name__]
        # print (cls.__name__, "creating", clz.__name__)
        Singleton.instances[clz.__name__] = clz()
        Singleton.first = clz
        return type(clz.__name__, (Singleton,), dict(clz.__dict__))