#!/usr/bin/env bash

echo "install required system packages: $(date)"
sudo yum install -y git

echo "install Python 2.7 libraries: $(date)"
sudo ${CONDA_BIN}/conda create --yes -p ${CONDA_2_7_HOME} python=2.7

sudo ${CONDA_2_7_BIN}/conda install --yes -n python2.7 \
  backports==1.0 \
  backports_abc==0.4 \
  boto3==1.3.1 \
  botocore==1.4.17 \
  cairo==1.12.18 \
  configparser==3.5.0b2 \
  cycler==0.10.0 \
  decorator==4.0.9 \
  docutils==0.12 \
  entrypoints==0.2 \
  fontconfig==2.11.1 \
  freetype==2.5.5 \
  functools32==3.2.3.2 \
  futures==3.0.5 \
  get_terminal_size==1.0.0 \
  ipykernel==4.3.1 \
  ipython==4.2.0 \
  ipython_genutils==0.1.0 \
  ipywidgets==4.1.1 \
  jinja2==2.8 \
  jmespath==0.9.0 \
  jsonschema==2.5.1 \
  jupyter==1.0.0 \
  libgcc==5.2.0 \
  libgfortran==3.0.0 \
  libpng==1.6.17 \
  libsodium==1.0.3 \
  libxml2==2.9.2 \
  markupsafe==0.23 \
  matplotlib==1.5.1 \
  mistune==0.7.2 \
  mkl==11.3.3 \
  nbconvert==4.2.0 \
  nbformat==4.0.1 \
  networkx==1.11 \
  notebook==4.2.0 \
  numpy==1.11.1 \
  openssl==1.0.2h \
  pandas==0.18.1 \
  path.py==8.2.1 \
  pexpect==4.0.1 \
  pickleshare==0.5 \
  pip==8.1.1 \
  pixman==0.32.6 \
  ptyprocess==0.5 \
  pycairo==1.10.0 \
  pydot==1.0.28 \
  pygments==2.1.3 \
  pyparsing==1.5.6 \
  pyqt==4.11.4 \
  python==2.7.11 \
  python-dateutil==2.5.3 \
  pytz==2016.4 \
  pyyaml==3.11 \
  pyzmq==15.2.0 \
  qt==4.8.7 \
  qtconsole==4.2.1 \
  readline==6.2 \
  scikit-learn==0.17.1 \
  scipy==0.18.0 \
  setuptools==20.7.0 \
  simplegeneric==0.8.1 \
  singledispatch==3.4.0.3 \
  sip==4.16.9 \
  six==1.10.0 \
  sqlite==3.9.2 \
  ssl_match_hostname==3.4.0.2 \
  terminado==0.5 \
  tk==8.5.18 \
  tornado==4.3 \
  traitlets==4.2.1 \
  wheel==0.29.0 \
  yaml==0.1.6 \
  zeromq==4.1.3 \
  zlib==1.2.8 \
  hdf5==1.8.16 \
  h5py==2.6.0 \
  patsy==0.4.1\
  ipywidgets==4.1.1 \
  pil==1.1.7 \
  lcms==1.19 \
  dill==0.2.5 \
  cffi==1.8.3 \
  cryptography==1.5.2 \
  enum34==1.1.6 \
  idna==2.1 \
  ipaddress==1.0.17 \
  paramiko==2.0.2 \
  pyasn1==0.1.9 \
  pycparser==2.14 \
  xlrd==1.0.0 \
  geos==3.5.0 \
  shapely==1.5.16

sudo ${CONDA_2_7_BIN}/conda install --yes -c jjhelmus -n python2.7 \
  pyproj=1.9.3

sudo ${CONDA_2_7_BIN}/conda install --yes -c conda-forge -n python2.7 \
  exifread=2.1.2

export R_HOME=/${CONDA_HOME}/lib/R
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${CONDA_HOME}/lib/R/lib
sudo ${CONDA_2_7_BIN}/conda install --yes -c r -n python2.7 \
  rpy2==2.7.0

# prepare theano installation with pip
mkdir -p /mnt/theano_tmp
sudo ln -sf /mnt/theano_tmp /home/.theano

sudo ${CONDA_2_7_BIN}/pip install \
  theano==0.8.0rc1 \
  datasketch==0.2.4 \
  spectra==0.0.7 \
  colormath==2.1.1 \
  gmplot==1.1.0 \
  slacker==0.9.24 \
  argparse==1.4.0

#sudo ${CONDA_2_7_BIN}/pip install -Iv git+git://github.com/fchollet/keras.git@654404c2ed8db47a5361a3bff9126a16507c9c4c

### install tensorflow, TODO: make sure to hardcode version of every dependency

#wget https://storage.googleapis.com/tensorflow/linux/cpu/tensorflow-0.10.0-cp27-none-linux_x86_64.whl
#sudo $CONDA_2_7_BIN/pip install tensorflow-0.10.0-cp27-none-linux_x86_64.whl
#rm tensorflow-0.10.0-cp27-none-linux_x86_64.whl
